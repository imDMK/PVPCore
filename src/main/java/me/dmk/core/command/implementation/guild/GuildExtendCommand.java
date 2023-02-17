package me.dmk.core.command.implementation.guild;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.gui.ConfirmationGui;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.guild.treasury.GuildTreasury;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.task.executor.TaskExecutor;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.SymbolUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by DMK on 28.01.2023
 */

@AllArgsConstructor

@Route(name = "guild extend")
public class GuildExtendCommand {

    private final PluginConfiguration pluginConfiguration;
    private final NotificationController notificationController;
    private final GuildController guildController;
    private final ProfileCache profileCache;
    private final TaskExecutor taskExecutor;

    @Execute
    void execute(Player player) {
        Profile profile = this.profileCache.getOrElseThrow(player.getUniqueId());

        Optional<Guild> guildOptional = profile.getGuild();
        if (guildOptional.isEmpty()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie posiadasz gildii<dark_gray>."
            );
            return;
        }

        Guild guild = guildOptional.get();
        GuildTreasury guildTreasury = guild.getGuildTreasury();

        if (!guild.isLeaderOrCoLeader(player.getUniqueId())) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie posiadasz uprawnień gildyjnych<dark_gray>."
            );
            return;
        }

        int coinsToExtendGuild = this.pluginConfiguration.getCoinsToExtendGuild();

        boolean canExtend = guildTreasury.getCoins() > coinsToExtendGuild;
        boolean playerCanExtend = profile.getProfileStatistics().getCoins() > coinsToExtendGuild;

        if (!canExtend && !playerCanExtend) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Brak wystarczającej ilości monet w skarbcu gildyjnym<dark_gray>, <red>aby przedłużyć gildię<dark_gray>."
            );
            return;
        }

        int coinsDifference = coinsToExtendGuild - guildTreasury.getCoins();

        new ConfirmationGui(player)
                .create(SymbolUtil.getCircle("<dark_gray>") + " <light_purple>Potwierdź przedłużenie gildii " + SymbolUtil.getCircle("<dark_gray>"))
                .afterConfirm(event -> {
                    if (coinsToExtendGuild > 0) {
                        if (!canExtend) {
                            Bukkit.dispatchCommand(player, "guild deposit " + coinsDifference);
                        }

                        guildTreasury.removeCoins(coinsToExtendGuild);
                    }

                    guild.extend(14);

                    this.taskExecutor.runAsync(
                            () -> this.guildController.save(guild)
                    );

                    this.notificationController.sendMessage(guild,
                            StringFormatter.formatGuild() + " <green>Gildia została przedłużona przez " + player.getName() + "<dark_gray>."
                    );

                    player.closeInventory();
                })
                .closeAfterCancel()
                .open();
    }
}

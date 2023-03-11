package me.dmk.core.command.implementation.guild;

import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.gui.confirmation.ConfirmationGui;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.guild.rank.GuildRank;
import me.dmk.core.guild.treasury.GuildTreasury;
import me.dmk.core.profile.Profile;
import me.dmk.core.task.executor.TaskExecutor;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 28.01.2023
 */

@AllArgsConstructor

@Route(name = "guild extend")
public class GuildExtendCommand {

    private final PluginConfiguration pluginConfiguration;
    private final NotificationController notificationController;
    private final GuildController guildController;
    private final TaskExecutor taskExecutor;

    @Async
    @Execute
    void execute(Player player, Profile profile, Guild guild) {
        GuildRank guildRank = guild.getGuildRank(player.getUniqueId());

        if (!guild.isLeader(player.getUniqueId()) || !guildRank.isCanExtend()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie posiadasz uprawnień gildyjnych<dark_gray>."
            );
            return;
        }

        GuildTreasury guildTreasury = guild.getGuildTreasury();

        int coinsToExtendGuild = this.pluginConfiguration.getCoinsToExtendGuild();

        boolean guildCanExtend = guildTreasury.getCoins() > coinsToExtendGuild;
        boolean playerCanExtend = profile.getProfileStatistics().getCoins() > coinsToExtendGuild;

        if (!guildCanExtend && !playerCanExtend) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Brak wystarczającej ilości monet w skarbcu gildyjnym<dark_gray>, <red>aby przedłużyć gildię<dark_gray>."
            );
            return;
        }

        int coinsDifference = coinsToExtendGuild - guildTreasury.getCoins();

        new ConfirmationGui(player)
                .title("Przedłużenie gildii")
                .afterConfirm(event -> {
                    if (coinsToExtendGuild > 0) {
                        if (!guildCanExtend) {
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
                .open(true);
    }
}

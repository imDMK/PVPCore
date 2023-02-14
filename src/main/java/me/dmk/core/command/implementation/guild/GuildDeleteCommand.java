package me.dmk.core.command.implementation.guild;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.task.executor.TaskExecutor;
import me.dmk.core.gui.ConfirmationGui;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.util.StyleUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by DMK on 16.01.2023
 */

@AllArgsConstructor

@Route(name = "guild delete")
public class GuildDeleteCommand {

    private final NotificationController notificationController;
    private final GuildController guildController;
    private final ProfileCache profileCache;
    private final GuildCache guildCache;
    private final TaskExecutor taskExecutor;

    @Execute(required = 0)
    void execute(Player player) {
        Profile profile = this.profileCache.getOrElseThrow(player.getUniqueId());

        Optional<Guild> guildOptional = profile.getGuild();
        if (guildOptional.isEmpty()) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Nie posiadasz gildii<dark_gray>."
            );
            return;
        }

        final Guild guild = guildOptional.get();

        if (!guild.isLeader(player.getUniqueId())) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Nie posiadasz gildyjnych uprawnień<dark_gray>."
            );
            return;
        }

        new ConfirmationGui(player)
                .create(StyleUtil.getCircle() + " <light_purple>Potwierdź usunięcie gildii " + StyleUtil.getCircle())
                .afterConfirm(event -> {
                    this.taskExecutor.runAsync(() -> {
                        this.guildController.delete(guild);
                        this.guildCache.remove(guild);

                        Bukkit.getOnlinePlayers().forEach(online ->
                                this.notificationController.sendMessage(online,
                                        StyleUtil.getWarning() + " <gray>Gildia " + StyleUtil.formatGuildTagAndName(guild) + " <gray>została <red>usunięta <gray>przez <light_purple>" + player.getName() + "<dark_gray>."
                                )
                        );
                    });

                    profile.setGuildTag(null);
                    player.closeInventory();
                })
                .closeAfterCancel()
                .open();
    }
}

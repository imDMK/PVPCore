package me.dmk.core.command.implementation.guild;

import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.chat.notification.PluginMessageType;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 08.01.2023
 */

@AllArgsConstructor

@Route(name = "guild leave")
public class GuildLeaveCommand {

    private final NotificationController notificationController;
    private final ProfileController profileController;
    private final GuildController guildController;

    @Async
    @Execute
    void execute(Player player, Profile profile, Guild guild) {
        if (guild.isLeader(player.getUniqueId())) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Jesteś liderem tej gildii<dark_gray>."
            );
            return;
        }

        guild.leaveMembership(player.getUniqueId());
        profile.setGuildTag(null);

        this.guildController.save(guild);
        this.profileController.save(profile);

        this.notificationController.sendGlobalPluginMessage(
                PluginMessageType.GUILD,
                StringFormatter.formatWarning() + " <gray>Gracz <light_purple>" + player.getName() + " <red>opuścił <gray>gildię <light_purple>" + guild.getTag() + "<dark_gray>."
        );
    }
}

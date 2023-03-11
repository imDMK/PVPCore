package me.dmk.core.command.implementation.guild;

import dev.rollczi.litecommands.argument.Arg;
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

@Route(name = "guild join")
public class GuildJoinCommand {

    private final NotificationController notificationController;
    private final ProfileController profileController;
    private final GuildController guildController;

    @Async
    @Execute(required = 1)
    void execute(Player player, Profile profile, @Arg Guild guild) {
        if (!guild.isInvitedToMembership(player.getUniqueId())) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie otrzymałeś/aś zaproszenia do tej gildii<dark_gray>."
            );
            return;
        }

        if (profile.getGuild().isPresent()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Posiadasz już gildię<dark_gray>... oszust?"
            );
            return;
        }

        guild.joinToMembership(player.getUniqueId());
        profile.setGuildTag(guild.getTag());

        this.guildController.save(guild);
        this.profileController.save(profile);

        this.notificationController.sendGlobalPluginMessage(
                PluginMessageType.GUILD,
                StringFormatter.formatWarning() + " <gray>Gracz <light_purple>" + player.getName() + " <green>dołączył <gray>do gildii <light_purple>" + guild.getTag() + "<dark_gray>."
        );
    }
}

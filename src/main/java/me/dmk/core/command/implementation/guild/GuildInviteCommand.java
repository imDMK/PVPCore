package me.dmk.core.command.implementation.guild;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.guild.Guild;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.StringUtil;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

/**
 * Created by DMK on 08.01.2023
 */

@AllArgsConstructor

@Route(name = "guild invite")
public class GuildInviteCommand {

    private final NotificationController notificationController;
    private final ProfileCache profileCache;

    @Async
    @Execute(required = 1)
    void execute(Player player, @Arg Profile other) {
        Profile profile = this.profileCache.getOrElseThrow(player.getUniqueId());

        Optional<Guild> guildOptional = profile.getGuild();

        if (guildOptional.isEmpty()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie posiadasz gildii<dark_gray>."
            );
            return;
        }

        final Guild guild = guildOptional.get();

        if (!guild.isLeaderOrCoLeader(player.getUniqueId())) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie posiadasz gildyjnych uprawnień<dark_gray>."
            );
            return;
        }

        if (guild.isMember(other.getUuid())) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Gracz jest już członkiem w twojej gildii<dark_gray>."
            );
            return;
        }

        if (guild.isInvited(other.getUuid())) {
            guild.cancelInvite(other.getUuid());

            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Anulowano <gray>zaproszenie do gildii gracza <light_purple>" + other.getName() + "<dark_gray>."
            );
            return;
        }

        other.getPlayer().ifPresent(otherPlayer ->
                this.notificationController.sendMessage(otherPlayer, List.of(
                        StringFormatter.formatWarning() + " <gray>Otrzymano zaproszenie do gildii o tagu <light_purple>" + guild.getTag() + " <gray>oraz nazwie <light_purple>" + guild.getName() + "<dark_gray>.",
                        "<dark_gray>- <click:run_command:/guild join " + guild.getTag() + ">" + StringFormatter.formatSuccess() + " <dark_gray><- <gray>Kliknij, aby <green>zaakceptować<dark_gray>."
                ))
        );

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringUtil.getGreenGradient() + " Zaproszono <gray>gracza <light_purple>" + other.getName() + " <gray>do gildii<dark_gray>."
        );

        guild.invite(other.getUuid());
    }
}

package me.dmk.core.command.implementation.guild;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.rank.GuildRank;
import me.dmk.core.profile.Profile;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by DMK on 08.01.2023
 */

@AllArgsConstructor

@Route(name = "guild invite")
public class GuildInviteCommand {

    private final NotificationController notificationController;

    @Async
    @Execute(required = 1)
    void execute(Player player, Guild guild, @Arg Profile other) {
        GuildRank guildRank = guild.getGuildRank(player.getUniqueId());
        
        if (!guild.isLeader(player.getUniqueId()) || !guildRank.isCanManageMembers()) {
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

        if (guild.isInvitedToMembership(other.getUuid())) {
            guild.declineInviteToMembership(other.getUuid());

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
                StringFormatter.formatSuccess() + StringFormatter.formatGreenGradient() + " Zaproszono <gray>gracza <light_purple>" + other.getName() + " <gray>do gildii <dark_gray>(<red>zaproszenie wygaśnie za 30 minut<dark_gray>)."
        );

        guild.inviteToMembership(other.getUuid());
    }
}

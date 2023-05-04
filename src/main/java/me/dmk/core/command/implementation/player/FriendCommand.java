package me.dmk.core.command.implementation.player;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.profile.Profile;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by DMK on 13.03.2023
 */

@AllArgsConstructor

@Route(name = "friend")
public class FriendCommand {

    private final NotificationController notificationController;

    @Async
    @Execute(route = "invite", required = 1)
    void executeInvite(Player player, Profile profile, @Arg Profile otherProfile) {
        if (profile.getUuid().equals(otherProfile.getUuid())) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie możesz zaprosić samego siebie do przyjaciół<dark_gray>...serio?"
            );
            return;
        }

        if (profile.isFriend(otherProfile.getUuid())) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Jesteście już przyjaciółmi<dark_gray>."
            );
            return;
        }

        if (profile.hasInviteToFriendsFrom(otherProfile.getUuid())) {
            Bukkit.dispatchCommand(player, "friend accept " + profile.getName());
            return;
        }

        if (otherProfile.hasInviteToFriendsFrom(profile.getUuid())) {
            otherProfile.removeInviteToFriends(profile.getUuid());

            this.notificationController.sendMessage(player,
                    StringFormatter.formatSuccess() + " <green>Anulowano <gray>zaproszenie do przyjaciół <gray>zaproszenie do przyjaciół od <light_purple>" + player.getName() + "<dark_gray>."
            );
            return;
        }

        otherProfile.receiveInviteToFriends(profile.getUuid());

        otherProfile.getPlayer().ifPresent(other ->
                this.notificationController.sendMessage(other, List.of(
                        StringFormatter.formatWarning() + " <gray>Otrzymano zaproszenie do przyjaźni od gracza <light_purple>" + player.getName() + "<dark_gray>.",
                        "<dark_gray>- <click:run_command:/friend accept " + player.getName() + ">" + StringFormatter.formatSuccess() + " <dark_gray><- <gray>Kliknij, aby <green>zaakceptować<dark_gray>."
                ))
        );

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + " <green>Zaproszono <gray>gracza <light_purple>" + otherProfile.getName() + " <gray>do przyjaciół<dark_gray>."
        );
    }

    @Async
    @Execute(route = "accept", required = 1)
    void executeAccept(Player player, Profile profile, @Arg Profile otherProfile) {
        if (profile.isFriend(otherProfile.getUuid())) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Jesteście już przyjaciółmi<dark_gray>."
            );
            return;
        }

        if (!profile.hasInviteToFriendsFrom(otherProfile.getUuid())) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie otrzymano zaproszenia od tego gracza<dark_gray>."
            );
            return;
        }

        profile.acceptInviteToFriends(otherProfile.getUuid());
        otherProfile.addFriend(profile.getUuid());

        otherProfile.getPlayer().ifPresent(other ->
                this.notificationController.sendMessage(other,
                        StringFormatter.formatWarning() + " <gray>Gracz <light_purple>" + player.getName() + " <green>zaakceptował <gray>zaproszenie o przyjaźń<dark_gray>."
                )
        );

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + " <green>Zaakceptowano <gray>zaproszenie gracza <light_purple>" + otherProfile.getName() + " <gray>do przyjaciół<dark_gray>."
        );
    }

    @Async
    @Execute(route = "break", required = 1)
    void executeBreak(Player player, Profile profile, @Arg Profile otherProfile) {
        if (!profile.isFriend(otherProfile.getUuid())) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie jesteście przyjaciółmi<dark_gray>."
            );
            return;
        }

        profile.removeFriend(otherProfile.getUuid());
        otherProfile.removeFriend(profile.getUuid());

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + " <green>Zerwano <gray>przyjaźń z graczem <light_purple>" + otherProfile.getName() + " <dark_gray>."
        );
    }
}

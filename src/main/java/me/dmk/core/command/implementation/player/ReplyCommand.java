package me.dmk.core.command.implementation.player;

import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.event.PrivateMessageEvent;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "reply", aliases = "r")
public class ReplyCommand {

    private final NotificationController notificationController;
    private final ProfileController profileController;

    @Execute(min = 1)
    void execute(Player player, Profile profile, @Joiner @Name("message") String message) {
        ProfileSettings profileSettings = profile.getProfileSettings();

        UUID lastPrivateMessage = profileSettings.getLastPrivateMessage();
        if (lastPrivateMessage == null) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie posiadasz ostatniego rozmówcy<dark_gray>."
            );
            return;
        }

        Player other = Bukkit.getServer().getPlayer(lastPrivateMessage);
        if (other == null) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Gracz jest offline<dark_gray>."
            );
            return;
        }

        Profile otherProfile = this.profileController.getOrElseThrow(other);

        PrivateMessageEvent privateMessageEvent = new PrivateMessageEvent(player, profile, other, otherProfile, message);
        Bukkit.getPluginManager().callEvent(privateMessageEvent);

        if (privateMessageEvent.isCancelled()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " " + privateMessageEvent.getCancelMessage()
            );
        }
    }
}

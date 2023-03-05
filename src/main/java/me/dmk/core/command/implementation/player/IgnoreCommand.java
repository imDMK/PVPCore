package me.dmk.core.command.implementation.player;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "ignore")
public class IgnoreCommand {

    private final NotificationController notificationController;
    private final ProfileCache profileCache;

    @Execute(required = 0)
    void execute(Player player, Profile profile) {
        ProfileSettings profileSettings = profile.getProfileSettings();

        profileSettings.setPrivateMessages(!profileSettings.isPrivateMessages());

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + " <gray>Prywatne wiadomości zostały " + StringFormatter.formatBoolean(profileSettings.isPrivateMessages(), 'e') + "<dark_gray>."
        );
    }

    @Execute(required = 1)
    void executeIgnoreOther(Player player, @Arg Profile otherProfile) {
        Profile profile = this.profileCache.getOrElseThrow(player);
        ProfileSettings profileSettings = profile.getProfileSettings();

        if (player.getUniqueId().equals(otherProfile.getUuid())) {
            Bukkit.dispatchCommand(player, "ignore");
            return;
        }

        if (profileSettings.getIgnoredPlayers().contains(otherProfile.getUuid())) {
            profileSettings.getIgnoredPlayers().remove(otherProfile.getUuid());

            this.notificationController.sendMessage(player,
                    StringFormatter.formatSuccess() + StringUtil.getGreenGradient() + " Odblokowano <gray>gracza <light_purple>" + otherProfile.getName() + "<dark_gray>."
            );
        } else {
            profileSettings.getIgnoredPlayers().add(otherProfile.getUuid());

            this.notificationController.sendMessage(player,
                    StringFormatter.formatSuccess() + StringUtil.getRedGradient() + " Zablokowano <gray>gracza <light_purple>" + otherProfile.getName() + "<dark_gray>."
            );
        }
    }
}

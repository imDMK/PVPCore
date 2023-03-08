package me.dmk.core.profile.settings.refresher;

import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.profile.Profile;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 08.03.2023
 */

@AllArgsConstructor
public class VanishRefresher {

    private final NotificationController notificationController;

    public void refresh(Player player, Profile profile) {
        if (profile.getProfileSettings().isVanish()) {
            if (!player.hasPermission("core.command.vanish")) {
                profile.getProfileSettings().setVanish(false);
                player.kickPlayer("&cTwoje uprawnienia uległy zmianie&8.");
                return;
            }

            this.notificationController.sendActionBar(player, "<gradient:light_purple:dark_purple>Jesteś niewidzialny");
        }
    }
}

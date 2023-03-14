package me.dmk.core.listener;

import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

/**
 * Created by DMK on 05.01.2023
 */

@AllArgsConstructor
public class PlayerLevelChangeListener implements Listener {

    private final NotificationController notificationController;
    private final ProfileController profileController;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        Player player = event.getPlayer();

        int newLevel = event.getNewLevel();

        Profile profile = this.profileController.getOrElseThrow(player);

        profile.getProfileStatistics().setLevel(newLevel);

        this.notificationController.sendMessage(player,
                StringFormatter.formatWarning() + StringFormatter.formatGreenGradient() + " Gratulacje! Awansowałeś na następny poziom<dark_gray>."
        );
    }
}

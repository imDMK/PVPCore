package me.dmk.core.profile.settings.task;

import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.util.string.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 05.01.2023
 */

@AllArgsConstructor
public class VanishTask implements Runnable {

    private final NotificationController notificationController;
    private final ProfileCache profileCache;

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.profileCache.get(player.getUniqueId()).ifPresent(profile -> {
                if (profile.getProfileSettings().isVanish()) {
                    this.notificationController.sendActionBar(player, StringUtil.getPurpleGradient() + "Jesteś niewidzialny");
                }
            });
        }
    }
}

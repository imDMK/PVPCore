package me.dmk.core.listener;

import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.util.StyleUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by DMK on 06.01.2023
 */

@AllArgsConstructor
public class PlayerCommandPreprocessListener implements Listener {

    private final PluginConfiguration pluginConfiguration;
    private final NotificationController notificationController;
    private final ProfileCache profileCache;

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.profileCache.getOrElseThrow(player.getUniqueId());

        String message = event.getMessage().toLowerCase();

        if (profile.hasFight()) {
            boolean anyMatch = this.pluginConfiguration.getFightBlockedCommands()
                    .stream()
                    .anyMatch(message::startsWith);

            if (anyMatch) {
                this.notificationController.sendMessage(player,
                        StyleUtil.getError() + " <red>Nie możesz użyć tej komendy podczas walki<dark_gray>."
                );
                event.setCancelled(true);
            }
        }
    }
}

package me.dmk.core.listener.connection;

import lombok.AllArgsConstructor;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.task.executor.TaskExecutor;
import me.dmk.core.util.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor
public class PlayerQuitListener implements Listener {

    private final ProfileController profileController;
    private final ProfileCache profileCache;
    private final TaskExecutor taskExecutor;

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Player player = event.getPlayer();

        this.profileCache.get(player.getUniqueId()).ifPresent(profile -> this.onProfileQuit(player, profile));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();

        this.profileCache.get(player.getUniqueId()).ifPresent(profile -> this.onProfileQuit(player, profile));
    }

    private void onProfileQuit(Player player, Profile profile) {
        profile.getProfileSettings().getBoard().remove();
        profile.getProfileStatistics().setTimeSpent(PlayerUtil.getSecondsPlayed(player));

        if (profile.hasFight()) {
            player.setHealth(0.0);
            profile.getFight().clear();
        }

        this.taskExecutor.runAsync(
                () -> this.profileController.save(profile)
        );
    }
}

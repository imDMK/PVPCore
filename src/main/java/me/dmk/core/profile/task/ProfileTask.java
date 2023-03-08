package me.dmk.core.profile.task;

import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.fight.Fight;
import me.dmk.core.profile.fight.FightRefresher;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.profile.settings.refresher.VanishRefresher;
import me.dmk.core.task.executor.TaskExecutor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 08.03.2023
 */

@AllArgsConstructor
public class ProfileTask implements Runnable {

    private final PluginConfiguration pluginConfiguration;
    private final MiniMessage miniMessage;
    private final NotificationController notificationController;
    private final ProfileCache profileCache;
    private final TaskExecutor taskExecutor;

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = this.profileCache.getOrElseThrow(player);
            ProfileSettings settings = profile.getProfileSettings();
            Fight fight = profile.getFight();

            if (settings.isVanish()) {
                new VanishRefresher(this.notificationController)
                        .refresh(player, profile);
            }

            if (fight.hasFight() || fight.hadFight()) {
                new FightRefresher(this.pluginConfiguration, this.miniMessage,  this.notificationController, this.taskExecutor)
                        .refresh(player, profile);
            }
        }
    }
}

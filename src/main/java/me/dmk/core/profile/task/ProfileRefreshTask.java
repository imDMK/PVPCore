package me.dmk.core.profile.task;

import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
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

public class ProfileRefreshTask implements Runnable {

    private final ProfileController profileController;

    private final VanishRefresher vanishRefresher;
    private final FightRefresher fightRefresher;

    public ProfileRefreshTask(PluginConfiguration pluginConfiguration, MiniMessage miniMessage, NotificationController notificationController, ProfileController profileController, TaskExecutor taskExecutor) {
        this.profileController = profileController;

        this.vanishRefresher = new VanishRefresher(notificationController);
        this.fightRefresher = new FightRefresher(pluginConfiguration, miniMessage, notificationController, taskExecutor);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = this.profileController.getOrElseThrow(player);
            ProfileSettings settings = profile.getProfileSettings();
            Fight fight = profile.getFight();

            if (settings.isVanish()) {
                this.vanishRefresher.refresh(player, profile);
            }

            if (fight.hasFight() || fight.hadFight()) {
                this.fightRefresher.refresh(player, profile);
            }
        }
    }
}

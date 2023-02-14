package me.dmk.core.profile.fight;

import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.task.executor.TaskExecutor;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

/**
 * Created by DMK on 05.01.2023
 */

@AllArgsConstructor
public class FightTask implements Runnable {

    private final PluginConfiguration pluginConfiguration;
    private final NotificationController notificationController;
    private final ProfileCache profileCache;
    private final TaskExecutor taskExecutor;

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.profileCache.get(player.getUniqueId()).ifPresent(profile -> {
                Fight fight = profile.getFight();

                if (profile.hasFight()) {
                    Component bossBarName = this.notificationController.getMiniMessage().deserialize(this.pluginConfiguration.getFightBossBarName().replace("<seconds>", String.valueOf(fight.expireToSeconds())));

                    BossBar bossBar = fight.getBossBar();
                    bossBar.name(bossBarName);
                    bossBar.progress(fight.expireToBossBarFloat());

                    if (fight.expireToSeconds() > 10) {
                        bossBar.color(BossBar.Color.RED);
                    } else {
                        bossBar.color(BossBar.Color.YELLOW);
                    }
                } else if (profile.wasFight()) {
                    Component bossBarName = this.notificationController.getMiniMessage().deserialize("<green>Skończyłeś/aś walczyć - możesz się wylogować");

                    BossBar bossBar = fight.getBossBar();
                    bossBar.name(bossBarName);
                    bossBar.color(BossBar.Color.GREEN);

                    fight.clear();

                    this.taskExecutor.runLaterAsync(
                            () -> this.notificationController.hideBossBar(player, bossBar), 1L, TimeUnit.SECONDS
                    );
                }
            });
        }
    }
}

package me.dmk.core.listener;

import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.guild.Guild;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.fight.Fight;
import me.dmk.core.profile.settings.ProfileSettings;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Optional;

/**
 * Created by DMK on 05.01.2023
 */

@AllArgsConstructor
public class EntityDamageByEntityListener implements Listener {

    private final PluginConfiguration pluginConfiguration;
    private final NotificationController notificationController;
    private final ProfileCache profileCache;

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!(event.getDamager() instanceof Player damager)) {
            return;
        }

        if (player.equals(damager)) {
            return;
        }

        Profile playerProfile = this.profileCache.getOrElseThrow(player.getUniqueId());
        ProfileSettings playerSettings = playerProfile.getProfileSettings();

        Profile damagerProfile = this.profileCache.getOrElseThrow(damager.getUniqueId());

        if (playerSettings.isGod()) {
            event.setCancelled(true);
            return;
        }

        Optional<Guild> guild = playerProfile.getGuild();
        if (guild.isPresent()) {
            if (guild.get().isMember(damager.getUniqueId())) {
                event.setCancelled(true);
                return;
            }
        }

        if (!damager.hasPermission("core.fight.bypass")) {
            Fight playerFight = playerProfile.getFight();
            Fight damagerFight = damagerProfile.getFight();

            if (!playerProfile.hasFight()) {
                this.createBossBar(player, playerFight);
            }

            if (!damagerProfile.hasFight()) {
                this.createBossBar(damager, damagerFight);
            }

            playerFight.put(damager.getUniqueId());
            damagerFight.put(player.getUniqueId());
        }
    }

    private void createBossBar(Player player, Fight fight) {
        Component bossBarName = this.notificationController.getMiniMessage().deserialize(this.pluginConfiguration.getFightBossBarName().replace("<seconds>", String.valueOf(this.pluginConfiguration.getFightTime())));
        BossBar bossBar = BossBar.bossBar(bossBarName, BossBar.MAX_PROGRESS, BossBar.Color.RED, BossBar.Overlay.PROGRESS);

        this.notificationController.showBossBar(player, bossBar);
        fight.setBossBar(bossBar);
    }
}

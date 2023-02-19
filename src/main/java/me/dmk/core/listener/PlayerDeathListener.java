package me.dmk.core.listener;

import lombok.AllArgsConstructor;
import me.dmk.core.CorePlugin;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.guild.statistics.GuildStatistics;
import me.dmk.core.murder.MurderCache;
import me.dmk.core.murder.MurderType;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.fight.Fight;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.profile.settings.incognito.IncognitoSettings;
import me.dmk.core.profile.statistics.ProfileStatistics;
import me.dmk.core.util.MurderUtil;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by DMK on 05.01.2023
 */

@AllArgsConstructor
public class PlayerDeathListener implements Listener {

    private final NotificationController notificationController;
    private final ProfileCache profileCache;
    private final MurderCache murderCache;

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        event.setKeepLevel(true);
        event.setDroppedExp(0);

        Player victim = event.getEntity();

        Profile victimProfile = this.profileCache.getOrElseThrow(victim.getUniqueId());
        ProfileStatistics victimStatistics = victimProfile.getProfileStatistics();
        ProfileSettings victimSettings = victimProfile.getProfileSettings();

        Fight victimFight = victimProfile.getFight();
        IncognitoSettings victimIncognitoSettings = victimSettings.getIncognitoSettings();

        String victimName = victimIncognitoSettings.isEnabled() ? StringFormatter.formatIncognito(victimIncognitoSettings.getIdentifier()) : victim.getName();

        Optional<UUID> lastAttacker = victimFight.getLastAttacker();
        Player killer = lastAttacker.isPresent() ? Bukkit.getServer().getPlayer(lastAttacker.get()) : victim.getKiller();

        if (killer == null || killer.equals(victim)) {
            victimStatistics.increaseDeaths();
            this.hideBossBarAndRespawn(victim, victimProfile, victimFight);
            return;
        }

        Profile killerProfile = this.profileCache.getOrElseThrow(killer.getUniqueId());
        ProfileStatistics killerStatistics = killerProfile.getProfileStatistics();
        ProfileSettings killerSettings = killerProfile.getProfileSettings();
        IncognitoSettings killerIncognitoSettings = killerSettings.getIncognitoSettings();

        String killerName = killerIncognitoSettings.isEnabled() ? StringFormatter.formatIncognito(killerIncognitoSettings.getIdentifier()): killer.getName();

        if (this.murderCache.hasKilled(killer, victim)) {
            this.notificationController.sendMessage(victim,
                    StringFormatter.formatWarning() + " <red>Gracz ostatnio zabił cię<dark_gray>, <red>nie straciłeś/aś żadnych statystyk<dark_gray>."
            );
            this.notificationController.sendMessage(killer,
                    StringFormatter.formatWarning() + " <red>Ten gracz ostatnio został przez ciebie zabity/a <dark_gray>- <red>nie zyskałeś/aś żadnych statystyk<dark_gray>."
            );

            this.hideBossBarAndRespawn(victim, victimProfile, victimFight);
            return;
        }

        int addExp;
        if (killer.hasPermission("core.double.experience")) {
            addExp = 10;
        } else {
            addExp = 5;
        }

        boolean revenge = this.murderCache.hasKilled(victim, killer);
        MurderType murderType = MurderUtil.getMurderType(victim, victimStatistics, killer, revenge);

        int addPoints = MurderUtil.calulcateAddPoints(murderType, victimStatistics.getPoints(), killerStatistics.getPoints());
        int removePoints = MurderUtil.calculateRemovePoints(murderType, addPoints);

        killerStatistics.addPoints(addPoints);
        killerStatistics.increaseKills();

        killer.giveExp(addExp);

        victimStatistics.removePoints(removePoints);
        victimStatistics.increaseDeaths();

        this.checkForGuilds(victimProfile, killerProfile, addPoints, removePoints);

        if (killerSettings.isSounds()) {
            killer.playSound(killer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100, 100);
        }

        if (victimSettings.isDeathMessages()) {
            this.notificationController.sendTitle(victim, "", "<red>" + MurderUtil.formatMurderNotification(murderType));
        }

        for (Player online : Bukkit.getOnlinePlayers()) {
            this.profileCache.get(online.getUniqueId()).ifPresent(onlineProfile -> {
                if (onlineProfile.getProfileSettings().isDeathMessages()) {
                    this.notificationController.sendMessage(online,
                            MurderUtil.formatDeathMessage(victimName, removePoints, murderType, killerName, addPoints)
                    );
                }
            });
        }

        this.murderCache.add(killer, victim);
        this.hideBossBarAndRespawn(victim, victimProfile, victimFight);
    }

    private void hideBossBarAndRespawn(Player player, Profile profile, Fight fight) {
        if (profile.hasFight()) {
            this.notificationController.hideBossBar(player, fight.getBossBar());
            fight.clear();
        }

        player.getWorld().strikeLightningEffect(player.getLocation());
        Bukkit.getServer().getScheduler().runTaskLater(CorePlugin.getCorePlugin(),
                () -> player.spigot().respawn(), 1L);
    }

    private void checkForGuilds(Profile victimProfile, Profile killerProfile, int addRank, int removeRank) {
        victimProfile.getGuild().ifPresent(guild -> {
            GuildStatistics statistics = guild.getGuildStatistics();

            statistics.removeRank(removeRank);
            statistics.increaseDeaths();
        });

        killerProfile.getGuild().ifPresent(guild -> {
            GuildStatistics statistics = guild.getGuildStatistics();

            statistics.addRank(addRank);
            statistics.increaseKills();
        });
    }
}

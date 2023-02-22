package me.dmk.core.listener.connection;

import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.kit.KitMap;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.profile.settings.nametag.ColorNameType;
import me.dmk.core.profile.settings.nametag.CustomSuffixType;
import me.dmk.core.profile.statistics.ProfileStatistics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor
public class PlayerJoinListener implements Listener {

    private final PluginConfiguration pluginConfiguration;
    private final NotificationController notificationController;
    private final ProfileCache profileCache;
    private final GuildCache guildCache;
    private final KitMap kitMap;

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String name = player.getName();

        Profile profile = this.profileCache.getOrElseCreate(uuid, name);
        ProfileStatistics statistics = profile.getProfileStatistics();

        if (!name.equals(profile.getName())) {
            profile.setName(name);
        }

        if (statistics.getLevel() != player.getLevel()) {
            statistics.setLevel(player.getLevel());
        }

        profile.setLastJoin(new Date());
        statistics.increaseEntrances();

        this.checkPermissions(player, profile);

        Bukkit.getOnlinePlayers().forEach(online -> this.profileCache.get(online.getUniqueId())
                .ifPresent(onlineProfile -> profile.refreshVanish(online, onlineProfile))
        );

        List<String> welcomeMessage = this.pluginConfiguration.getWelcomeMessage();
        if (!welcomeMessage.isEmpty()) {
            this.notificationController.sendMessage(player, welcomeMessage);
        }

        this.profileCache.add(profile);
        profile.getGuild().ifPresent(this.guildCache::add);
    }

    private void checkPermissions(Player player, Profile profile) {
        ProfileSettings settings = profile.getProfileSettings();

        if (settings.getColorName() != ColorNameType.DEAFULT && !player.hasPermission("core.nametag.colorname")) {
            settings.setColorName(ColorNameType.DEAFULT);
        }

        if (settings.getCustomSuffix() != CustomSuffixType.NONE && !player.hasPermission("core.nametag.suffix")) {
            settings.setCustomSuffix(CustomSuffixType.NONE);
        }

        if (settings.isGod() && !player.hasPermission("core.command.god")) {
            settings.setGod(false);
        }

        if (settings.isVanish() && !player.hasPermission("core.command.vanish")) {
            settings.setVanish(false);
        }

        if (settings.getIncognitoSettings().isEnabled() && !player.hasPermission("core.command.incognito")) {
            settings.getIncognitoSettings().setEnabled(false);
        }
    }
}

package me.dmk.core.listener.player.connection;

import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.kit.KitMap;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.profile.settings.nametag.ColorNameType;
import me.dmk.core.profile.settings.nametag.CustomSuffixType;
import me.dmk.core.profile.statistics.ProfileStatistics;
import me.dmk.core.util.string.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Date;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor
public class PlayerJoinListener implements Listener {

    private final PluginConfiguration pluginConfiguration;
    private final NotificationController notificationController;
    private final ProfileController profileController;
    private final GuildController guildController;
    private final KitMap kitMap;

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();

        try {
            this.onProfileJoin(player);
        } catch (Exception exception) {
            player.kickPlayer(
                    StringUtil.colorLegacy("&cWystąpił błąd podczas ładowania twojego profilu<dark_gray>.")
            );
        }
    }

    private void onProfileJoin(Player player) {
        Profile profile = this.profileController.findByUUIDOrElseCreate(player.getUniqueId(), player.getName());
        ProfileStatistics statistics = profile.getProfileStatistics();

        profile.setLastJoin(new Date());
        statistics.increaseEntrances();

        if (!player.getName().equals(profile.getName())) {
            profile.setName(player.getName());
        }

        if (profile.getPlayerTime() != 0L) {
            player.setPlayerTime(profile.getPlayerTime(), false);
        }

        if (profile.getWeatherType() != null) {
            player.setPlayerWeather(profile.getWeatherType());
        }

        if (statistics.getLevel() != player.getLevel()) {
            player.setLevel(statistics.getLevel());
        }

        this.notificationController.sendMessage(player, this.pluginConfiguration.getWelcomeMessage());

        this.refreshVanish(player, profile);
        this.checkPermissions(player, profile);
        this.kitMap.addPlayerKit(player, statistics);

        profile.getGuild().ifPresent(this.guildController::add);
    }

    private void refreshVanish(Player player, Profile profile) {
        Bukkit.getOnlinePlayers().forEach(online ->
                this.profileController.get(online.getUniqueId()).ifPresent(onlineProfile ->
                        profile.refreshVanish(player, online, onlineProfile)
                )
        );
    }

    private void checkPermissions(Player player, Profile profile) {
        ProfileSettings settings = profile.getProfileSettings();

        if (player.getGameMode() != GameMode.SURVIVAL && !player.hasPermission("core.command.gamemode")) {
            player.setGameMode(GameMode.SURVIVAL);
        }

        if (player.getAllowFlight() && !player.hasPermission("core.command.fly")) {
            player.setAllowFlight(false);
        }

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

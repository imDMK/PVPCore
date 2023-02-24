package me.dmk.core.command.implementation.admin;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "vanish", aliases = "v")
@Permission("core.command.vanish")
public class VanishCommand {

    private final NotificationController notificationController;
    private final ProfileCache profileCache;

    @Execute
    void execute(Player player) {
        Profile profile = this.profileCache.getOrElseThrow(player);
        ProfileSettings profileSettings = profile.getProfileSettings();

        if (profileSettings.isVanish()) {
            profileSettings.setVanish(false);

            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        } else {
            profileSettings.setVanish(true);

            PotionEffect potionEffect = new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, true, false);
            player.addPotionEffect(potionEffect);
        }

        Bukkit.getOnlinePlayers().forEach(online -> this.profileCache.get(online.getUniqueId())
                .ifPresent(onlineProfile -> profile.refreshVanish(player, online, onlineProfile))
        );

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + " <gray>Twój tryb niewidzialności został " + StringFormatter.formatBoolean(profileSettings.isVanish()) + "<dark_gray>."
        );
    }
}

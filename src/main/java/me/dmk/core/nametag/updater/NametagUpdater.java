package me.dmk.core.nametag.updater;

import lombok.AllArgsConstructor;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.nametag.Nametag;
import me.dmk.core.nametag.map.NametagMap;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.profile.settings.incognito.IncognitoSettings;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by DMK on 04.04.2023
 */

@AllArgsConstructor
public class NametagUpdater {

    private final LuckPermsController luckPermsController;
    private final ProfileController profileController;
    private final NametagMap nametagMap;

    public void updateAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Nametag nametag = this.nametagMap.getOrElseCreate(player);

            for (Player online : Bukkit.getOnlinePlayers()) {
                this.update(player, nametag, online);
            }
        }
    }

    public void update(Player player, Nametag nametag, Player other) {
        Profile playerProfile = this.profileController.getOrElseThrow(player);
        Profile otherProfile = this.profileController.getOrElseThrow(other);

        ProfileSettings playerSettings = playerProfile.getProfileSettings();
        IncognitoSettings playerIncognitoSettings = playerSettings.getIncognitoSettings();

        Optional<String> groupOptional = this.luckPermsController.getHighestGroupPrefix(player.getUniqueId());
        Optional<String> guildTagOptional = StringFormatter.formatGuildTag(
                playerProfile.getGuild().orElse(null),
                other,
                otherProfile.getGuild().orElse(null)
        );

        String suffix = guildTagOptional.orElse("");

        if (playerSettings.isVanish() && other.hasPermission("core.command.vanish")) {
            suffix += (suffix.isEmpty() ? StringFormatter.formatVanish() : " " + StringFormatter.formatVanish());
        }

        Optional<String> suffixOptional = Optional.of(suffix).filter(string -> !string.isEmpty());

        groupOptional.ifPresentOrElse(nametag::setPrefix, nametag::resetPrefix);
        suffixOptional.ifPresentOrElse(nametag::setSuffix, nametag::resetSuffix);

        if (playerIncognitoSettings.isEnabled()) {
            if (other.hasPermission("core.command.incognito.find")) {
                nametag.setVisibility("always");
            } else {
                nametag.setVisibility("hideForOtherTeams");
            }
        } else {
            if (nametag.getVisibility().equals("hideForOtherTeams")) {
                nametag.setVisibility("always");
            }
        }

        nametag.send(other);
    }
}

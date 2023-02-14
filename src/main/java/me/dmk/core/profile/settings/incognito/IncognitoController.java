package me.dmk.core.profile.settings.incognito;

import com.mongodb.client.model.Filters;
import lombok.RequiredArgsConstructor;
import me.dmk.core.database.data.MongoDataService;
import me.dmk.core.profile.Profile;
import net.skinsrestorer.api.PlayerWrapper;
import net.skinsrestorer.api.SkinsRestorerAPI;
import net.skinsrestorer.api.exception.SkinRequestException;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by DMK on 12.01.2023
 */

@RequiredArgsConstructor
public class IncognitoController {

    private final MongoDataService mongoDataService;
    private final SkinsRestorerAPI skinsRestorerAPI;

    public boolean changeSkin(Player player, Profile profile) {
        IncognitoSettings incognitoSettings = profile.getProfileSettings().getIncognitoSettings();

        if (incognitoSettings.isEnabled()) {
            this.skinsRestorerAPI.removeSkin(player.getName());

            try {
                this.skinsRestorerAPI.applySkin(new PlayerWrapper(player));
            } catch (SkinRequestException requestException) {
                return false;
            }
        } else {
            try {
                this.skinsRestorerAPI.setSkin(player.getName(), "wither_skeleton");
                this.skinsRestorerAPI.applySkin(new PlayerWrapper(player));
            } catch (SkinRequestException requestException) {
                return false;
            }
        }

        incognitoSettings.toggle();
        return true;
    }

    public Optional<Profile> findProfileByIdentifier(String identifier) {
        Bson filters = Filters.eq("profileSettings.incognitoSettings.identifier", identifier);
        return this.mongoDataService.find(filters, Profile.class);
    }
}

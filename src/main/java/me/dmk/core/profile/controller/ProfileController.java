package me.dmk.core.profile.controller;

import com.mongodb.client.model.Filters;
import lombok.RequiredArgsConstructor;
import me.dmk.core.database.data.MongoDataService;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.util.string.StringUtil;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by DMK on 28.12.2022
 */

@RequiredArgsConstructor
public class ProfileController extends ProfileCache {

    private final MongoDataService mongoDataService;

    public Profile create(UUID uuid, String name) {
        Profile profile = new Profile(uuid, name);

        this.mongoDataService.insert(profile);
        this.add(profile);
        return profile;
    }

    public Profile getOrElseThrow(Player player) {
        Optional<Profile> profileOptional = this.get(player.getUniqueId());

        if (profileOptional.isEmpty() && player.isOnline()) {
            player.kickPlayer(
                    StringUtil.colorLegacy("&cWystąpił błąd podczas ładowania twojego proflu&8.")
            );
        }

        return profileOptional.orElseThrow();
    }

    public Profile findByUUIDOrElseCreate(UUID uuid, String name) {
        return this.findByUUID(uuid).orElseGet(
                () -> this.create(uuid, name)
        );
    }

    public Optional<Profile> findByUUID(UUID uuid) {
        Bson filters = Filters.eq("uuid", uuid.toString());

        Optional<Profile> profileOptional = this.mongoDataService.find(filters, Profile.class);
        profileOptional.ifPresent(this::add);

        return profileOptional;
    }

    public Optional<Profile> findByName(String name) {
        Bson filters = Filters.eq("name", name);

        Optional<Profile> profileOptional = this.mongoDataService.find(filters, Profile.class);
        profileOptional.ifPresent(this::add);

        return profileOptional;
    }

    public Optional<Profile> getOrElseLoad(UUID uuid) {
        Optional<Profile> profile = this.get(uuid);
        if (profile.isPresent()) {
            return profile;
        }

        return this.findByUUID(uuid);
    }

    public Optional<Profile> getOrElseLoad(String name) {
        Optional<Profile> profile = this.get(name);
        if (profile.isPresent()) {
            return profile;
        }

        return this.findByName(name);
    }

    public List<Profile> getTops(Bson sort, int limit) {
        return this.mongoDataService.sort(Profile.class, sort, limit);
    }

    public void save(Profile profile) {
        Bson filters = Filters.eq("uuid", profile.getUuid().toString());
        this.mongoDataService.save(filters, profile);
    }

    public void delete(Profile profile) {
        this.mongoDataService.delete(Filters.eq("uuid", profile.getUuid().toString()), profile);
        this.remove(profile);
    }
}

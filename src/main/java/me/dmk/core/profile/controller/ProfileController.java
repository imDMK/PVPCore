package me.dmk.core.profile.controller;

import com.mongodb.client.model.Filters;
import lombok.RequiredArgsConstructor;
import me.dmk.core.database.data.MongoDataService;
import me.dmk.core.profile.Profile;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by DMK on 28.12.2022
 */

@RequiredArgsConstructor
public class ProfileController {

    private final MongoDataService mongoDataService;

    public Profile create(UUID uuid, String name) {
        Profile profile = new Profile(uuid, name);

        this.mongoDataService.insert(profile);
        return profile;
    }

    public Optional<Profile> findByUUID(UUID uuid) {
        return this.mongoDataService.find(Filters.eq("uuid", uuid.toString()), Profile.class);
    }

    public Optional<Profile> findByName(String name) {
        return this.mongoDataService.find(Filters.eq("name", name), Profile.class);
    }

    public Profile findByUUIDOrCreate(UUID uuid, String name) {
        return this.findByUUID(uuid).orElseGet(() -> this.create(uuid, name));
    }

    public void save(Profile profile) {
        this.mongoDataService.save(Filters.eq("uuid", profile.getUuid().toString()), profile);
    }

    public void delete(Profile profile) {
        this.mongoDataService.delete(Filters.eq("uuid", profile.getUuid().toString()), profile);
    }

    public List<Profile> getTops(Bson sort, int limit) {
        return this.mongoDataService.sort(Profile.class, sort, limit);
    }
}

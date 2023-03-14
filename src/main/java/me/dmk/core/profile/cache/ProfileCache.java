package me.dmk.core.profile.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import me.dmk.core.profile.Profile;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by DMK on 29.12.2022
 */

@Getter
public class ProfileCache {

    private final Cache<UUID, Profile> uuidProfileCache = Caffeine.newBuilder()
            .expireAfterAccess(1L, TimeUnit.DAYS)
            .expireAfterWrite(1L, TimeUnit.DAYS)
            .build();

    private final Cache<String, Profile> nameProfileCache = Caffeine.newBuilder()
            .expireAfterAccess(1L, TimeUnit.DAYS)
            .expireAfterWrite(1L, TimeUnit.DAYS)
            .build();

    public Optional<Profile> get(UUID uuid) {
        return Optional.ofNullable(
                this.uuidProfileCache.asMap().get(uuid)
        );
    }

    public Optional<Profile> get(String name) {
        return Optional.ofNullable(
                this.nameProfileCache.asMap().get(name)
        );
    }

    public void add(Profile profile) {
        this.uuidProfileCache.put(profile.getUuid(), profile);
        this.nameProfileCache.put(profile.getName(), profile);
    }

    public void remove(Profile profile) {
        this.uuidProfileCache.asMap().remove(profile.getUuid());
        this.nameProfileCache.asMap().remove(profile.getName());
    }
}

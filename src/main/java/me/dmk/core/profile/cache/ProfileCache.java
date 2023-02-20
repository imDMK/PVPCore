package me.dmk.core.profile.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.util.string.StringUtil;
import org.bukkit.Bukkit;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by DMK on 29.12.2022
 */

@RequiredArgsConstructor
public class ProfileCache {

    private final ProfileController profileController;

    @Getter
    private final Cache<UUID, Profile> uuidProfileCache = Caffeine.newBuilder()
            .expireAfterAccess(12, TimeUnit.HOURS)
            .build();

    @Getter
    private final Cache<String, Profile> stringProfileCache = Caffeine.newBuilder()
            .expireAfterAccess(12, TimeUnit.HOURS)
            .build();

    public Optional<Profile> get(UUID uuid) {
        return Optional.ofNullable(this.uuidProfileCache.asMap().get(uuid));
    }

    public Optional<Profile> get(String name) {
        return Optional.ofNullable(this.stringProfileCache.asMap().get(name));
    }

    public Profile getOrElseThrow(UUID uuid) {
        Optional<Profile> profile = this.get(uuid);

        if (profile.isEmpty()) {
            Optional.ofNullable(Bukkit.getServer().getPlayer(uuid))
                    .ifPresent(
                            p -> p.kickPlayer(StringUtil.colorLegacy("&cWystąpił błąd podczas ładowania twojego proflu&8."))
                    );
        }

        return profile.orElseThrow();
    }

    public Profile getOrElseCreate(UUID uuid, String name) {
        return this.get(uuid).orElseGet(
                () -> this.profileController.findByUUIDOrCreate(uuid, name)
        );
    }

    public Optional<Profile> getOrElseLoad(String name) {
        Optional<Profile> profile = this.get(name);
        if (profile.isPresent()) {
            return profile;
        }

        return this.profileController.findByName(name);
    }

    public Optional<Profile> getOrElseLoad(UUID uuid) {
        Optional<Profile> profile = this.get(uuid);
        if (profile.isPresent()) {
            return profile;
        }

        return this.profileController.findByUUID(uuid);
    }

    public void add(Profile profile) {
        this.uuidProfileCache.put(profile.getUuid(), profile);
        this.stringProfileCache.put(profile.getName(), profile);
    }
}

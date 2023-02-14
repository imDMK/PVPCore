package me.dmk.core.chat.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.dmk.core.chat.GlobalChatSettings;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by DMK on 29.12.2022
 */

@NoArgsConstructor
public class GlobalChatCache {

    @Getter
    private final GlobalChatSettings globalChatSettings = new GlobalChatSettings(this);

    private Cache<UUID, Instant> instantCache = Caffeine.newBuilder()
            .expireAfterWrite(this.globalChatSettings.getDelay(), TimeUnit.SECONDS)
            .build();

    public void rebuildCache(long delay) {
        this.instantCache = Caffeine.newBuilder()
                .expireAfterWrite(delay, TimeUnit.SECONDS)
                .build();
    }

    public void put(UUID uuid) {
        this.instantCache.put(uuid, Instant.now().plusSeconds(this.globalChatSettings.getDelay()));
    }

    public Instant get(UUID uuid) {
        return this.instantCache.asMap().get(uuid);
    }

    public boolean canUseChat(UUID uuid) {
        return Instant.now().isAfter(this.instantCache.asMap().getOrDefault(uuid, Instant.MIN));
    }
}

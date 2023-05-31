package me.dmk.core.chat;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by DMK on 29.12.2022
 */

@Getter
@NoArgsConstructor
public class GlobalChatCache extends GlobalChatSettings {

    private Cache<UUID, Instant> chatCache = Caffeine.newBuilder()
            .expireAfterWrite(this.getDelay(), TimeUnit.SECONDS)
            .build();

    public void rebuildCache(long delay) {
        this.chatCache = Caffeine.newBuilder()
                .expireAfterWrite(delay, TimeUnit.SECONDS)
                .build();
    }

    public void put(UUID uuid) {
        this.chatCache.put(uuid, Instant.now().plusSeconds(this.getDelay()));
    }

    public Instant get(UUID uuid) {
        return this.chatCache.asMap().get(uuid);
    }

    public boolean canUseChat(UUID uuid) {
        return Instant.now().isAfter(this.chatCache.asMap().getOrDefault(uuid, Instant.MIN));
    }
}

package me.dmk.core.murder;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by DMK on 12.01.2023
 */

@Getter
public class MurderCache {

    private final Cache<UUID, UUID> murderCache = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();

    public void add(Player killer, Player victim) {
        this.murderCache.put(killer.getUniqueId(), victim.getUniqueId());
    }

    public boolean hasKilled(Player killer, Player victim) {
        return this.murderCache.asMap().getOrDefault(killer.getUniqueId(), killer.getUniqueId()).equals(victim.getUniqueId());
    }
}

package me.dmk.core.guild.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import me.dmk.core.guild.Guild;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by DMK on 07.01.2023
 */

@Getter
public class GuildCache {

    private final Cache<String, Guild> tagGuildCache = Caffeine.newBuilder()
            .expireAfterWrite(1L, TimeUnit.DAYS)
            .expireAfterAccess(1L, TimeUnit.DAYS)
            .build();

    public Optional<Guild> getByTag(String tag) {
        return Optional.ofNullable(
                this.tagGuildCache.asMap().get(tag)
        );
    }

    public void add(Guild guild) {
        this.tagGuildCache.put(guild.getTag(), guild);
    }

    public void remove(Guild guild) {
        this.tagGuildCache.asMap().remove(guild.getTag());
    }

    public Collection<Guild> getGuilds() {
        return this.tagGuildCache.asMap().values();
    }
}

package me.dmk.core.guild.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.controller.GuildController;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by DMK on 07.01.2023
 */

@RequiredArgsConstructor
public class GuildCache {

    private final GuildController guildController;

    @Getter
    private final Cache<String, Guild> stringGuildCache = Caffeine.newBuilder()
            .expireAfterAccess(1, TimeUnit.DAYS)
            .build();

    public Optional<Guild> getByTag(String tag) {
        return Optional.ofNullable(this.stringGuildCache.asMap().get(tag));
    }

    public Optional<Guild> getOrElseLoad(String tag) {
        Optional<Guild> guild = this.getByTag(tag);
        if (guild.isPresent()) {
            return guild;
        }

        return this.guildController.findByTag(tag);
    }

    public Collection<Guild> getGuilds() {
        return this.stringGuildCache.asMap().values();
    }

    public void add(Guild guild) {
        this.stringGuildCache.put(guild.getTag(), guild);
    }

    public void remove(Guild guild) {
        this.stringGuildCache.asMap().remove(guild.getTag());
    }
}

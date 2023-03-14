package me.dmk.core.guild.controller;

import com.mongodb.client.model.Filters;
import lombok.AllArgsConstructor;
import me.dmk.core.database.data.MongoDataService;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.cache.GuildCache;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Optional;

/**
 * Created by DMK on 07.01.2023
 */

@AllArgsConstructor
public class GuildController extends GuildCache {

    private final MongoDataService mongoDataService;

    public void create(Guild guild) {
        this.mongoDataService.insert(guild);
    }

    public Optional<Guild> findByTag(String tag) {
        Bson filters = Filters.eq("tag", tag);

        Optional<Guild> guildOptional = this.mongoDataService.find(filters, Guild.class);
        guildOptional.ifPresent(this::add);

        return guildOptional;
    }

    public Optional<Guild> getOrElseLoad(String tag) {
        Optional<Guild> guild = this.getByTag(tag);
        if (guild.isPresent()) {
            return guild;
        }

        return this.findByTag(tag);
    }

    public List<Guild> getTops(Bson sort, int limit) {
        return this.mongoDataService.sort(Guild.class, sort, limit);
    }

    public void save(Guild guild) {
        this.mongoDataService.save(Filters.eq("tag", guild.getTag()), guild);
    }

    public void delete(Guild guild) {
        this.mongoDataService.delete(Filters.eq("tag", guild.getTag()), guild);
        this.remove(guild);
    }
}

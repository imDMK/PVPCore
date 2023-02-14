package me.dmk.core.guild.controller;

import com.mongodb.client.model.Filters;
import lombok.AllArgsConstructor;
import me.dmk.core.database.data.MongoDataService;
import me.dmk.core.guild.Guild;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Optional;

/**
 * Created by DMK on 07.01.2023
 */

@AllArgsConstructor
public class GuildController {

    private final MongoDataService mongoDataService;

    public void create(Guild guild) {
        this.mongoDataService.insert(guild);
    }

    public void delete(Guild guild) {
        this.mongoDataService.delete(guild);
    }

    public Optional<Guild> findByTag(String tag) {
        return this.mongoDataService.find(Filters.eq("tag", tag), Guild.class);
    }

    public void save(Guild guild) {
        this.mongoDataService.save(Filters.eq("tag", guild.getTag()), guild);
    }

    public List<Guild> getTops(Bson sort, int limit) {
        return this.mongoDataService.sort(Guild.class, sort, limit);
    }
}

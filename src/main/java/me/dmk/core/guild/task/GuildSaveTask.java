package me.dmk.core.guild.task;

import lombok.AllArgsConstructor;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.guild.controller.GuildController;

/**
 * Created by DMK on 11.03.2023
 */

@AllArgsConstructor
public class GuildSaveTask implements Runnable {

    private final GuildController guildController;
    private final GuildCache guildCache;

    @Override
    public void run() {
        for (Guild guild : this.guildCache.getGuilds()) {
            this.guildController.save(guild);
        }
    }
}

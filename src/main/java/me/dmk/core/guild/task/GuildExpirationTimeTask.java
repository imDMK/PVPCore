package me.dmk.core.guild.task;

import com.mongodb.client.model.Filters;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.chat.notification.PluginMessageType;
import me.dmk.core.database.data.MongoDataService;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.util.string.StringFormatter;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

/**
 * Created by DMK on 08.03.2023
 */

@AllArgsConstructor
public class GuildExpirationTimeTask implements Runnable {

    private final MongoDataService mongoDataService;
    private final NotificationController notificationController;
    private final GuildController guildController;
    private final GuildCache guildCache;

    @Override
    public void run() {
        String currentTime = String.valueOf(System.currentTimeMillis());

        Bson filters = Filters.lt("expireAt", currentTime);
        List<Guild> expiredGuilds = this.mongoDataService.findAll(filters, Guild.class);

        for (Guild guild : expiredGuilds) {
            this.guildController.delete(guild);
            this.guildCache.remove(guild);

            Optional.ofNullable(Bukkit.getPlayer(guild.getLeader()))
                    .filter(Player::isOnline)
                    .ifPresent(player ->
                            this.notificationController.sendMessage(player,
                                    StringFormatter.formatWarning() + " <red>Twoja gildia wygasła<dark_gray>."
                            )
                    );

            this.notificationController.sendGlobalPluginMessage(PluginMessageType.GUILD,
                    StringFormatter.formatWarning() + " <gray>Gildia <light_purple>" + guild.getTag() + " <red>wygasła<dark_gray>."
            );
        }
    }
}

package me.dmk.core.listener.connection;

import lombok.AllArgsConstructor;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.punishment.PunishmentType;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor
public class PlayerLoginListener implements Listener {

    private final ProfileCache profileCache;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        this.profileCache.getOrElseLoad(player.getUniqueId())
                .flatMap(profile -> profile.getActivePunishment(PunishmentType.BAN))
                .ifPresent(punishment ->
                        event.disallow(PlayerLoginEvent.Result.KICK_BANNED, StringFormatter.formatBanMessage(punishment))
                );
    }
}

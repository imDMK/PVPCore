package me.dmk.core.listener.player;

import lombok.AllArgsConstructor;
import me.dmk.core.teleport.TeleportMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by DMK on 20.02.2023
 */

@AllArgsConstructor
public class PlayerMoveListener implements Listener {

    private final TeleportMap teleportMap;

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null) {
            return;
        }

        if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) {
            return;
        }

        this.teleportMap.ifTeleporting(player, this.teleportMap::removeTeleporting);
    }
}

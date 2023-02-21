package me.dmk.core.teleport;

import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Created by DMK on 20.02.2023
 */

public class TeleportMap {

    private final Set<UUID> teleportingPlayers = ConcurrentHashMap.newKeySet();

    public boolean isTeleporting(Player player) {
        return this.teleportingPlayers.contains(player.getUniqueId());
    }

    public void addTeleporting(Player player) {
        this.teleportingPlayers.add(player.getUniqueId());
    }

    public void removeTeleporting(Player player) {
        this.teleportingPlayers.remove(player.getUniqueId());
    }

    public void ifTeleporting(Player player, Consumer<Player> playerConsumer) {
        if (this.isTeleporting(player)) {
            playerConsumer.accept(player);
        }
    }
}

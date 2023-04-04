package me.dmk.core.nametag.map;

import com.google.common.collect.Maps;
import me.dmk.core.nametag.Nametag;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by DMK on 04.04.2023
 */

public class NametagMap {

    private final Map<UUID, Nametag> nametagMap = Maps.newConcurrentMap();

    public void put(UUID uuid, Nametag nametag) {
        this.nametagMap.put(uuid, nametag);
    }

    public Optional<Nametag> get(UUID uuid) {
        return Optional.ofNullable(
                this.nametagMap.get(uuid)
        );
    }

    public Nametag getOrElseCreate(Player player) {
        return this.get(player.getUniqueId())
                .orElseGet(() -> {
                    Nametag nametag = new Nametag(player);

                    this.put(player.getUniqueId(), nametag);

                    return nametag;
                });
    }

    public void remove(UUID uuid) {
        this.nametagMap.remove(uuid);
    }
}

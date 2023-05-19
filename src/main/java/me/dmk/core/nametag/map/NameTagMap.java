package me.dmk.core.nametag.map;

import com.google.common.collect.Maps;
import me.dmk.core.nametag.NameTag;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by DMK on 04.04.2023
 */

public class NameTagMap {

    private final Map<UUID, NameTag> nametagMap = Maps.newConcurrentMap();

    public void put(UUID uuid, NameTag nametag) {
        this.nametagMap.put(uuid, nametag);
    }

    public Optional<NameTag> get(UUID uuid) {
        return Optional.ofNullable(
                this.nametagMap.get(uuid)
        );
    }

    public NameTag getOrElseCreate(Player player) {
        return this.get(player.getUniqueId())
                .orElseGet(() -> this.create(player));
    }

    public NameTag create(Player player) {
        NameTag nametag = new NameTag(player);

        this.put(player.getUniqueId(), nametag);

        return nametag;
    }

    public void remove(UUID uuid) {
        this.nametagMap.remove(uuid);
    }
}

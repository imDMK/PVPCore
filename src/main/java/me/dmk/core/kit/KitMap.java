package me.dmk.core.kit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.dmk.core.configuration.KitConfiguration;
import me.dmk.core.profile.statistics.ProfileStatistics;
import me.dmk.core.util.PlayerUtil;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by DMK on 21.02.2023
 */

@RequiredArgsConstructor
public class KitMap {

    private final KitConfiguration kitConfiguration;

    @Getter
    private final Map<Integer, Kit> integerKitMap = new ConcurrentHashMap<>();

    public void loadKitsFromConfiguration() {
        this.kitConfiguration.getKitList().forEach(kit ->
                this.integerKitMap.put(kit.getLevel(), kit)
        );
    }

    public void addPlayerKit(Player player, ProfileStatistics statistics) {
        if (player.hasPermission("core.ignore.kit.receive"))  {
            return;
        }

        this.get(statistics.getKitLevel()).ifPresentOrElse(kit -> {
            player.getInventory().clear();
            PlayerUtil.addItems(player, kit.getItems());
        }, () -> statistics.setKitLevel(1));
    }

    public Optional<Kit> get(int level) {
        return Optional.ofNullable(this.integerKitMap.get(level));
    }
}

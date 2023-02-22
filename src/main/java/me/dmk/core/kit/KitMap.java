package me.dmk.core.kit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.dmk.core.configuration.KitConfiguration;

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
    private final Map<Integer, Kit> kitMap = new ConcurrentHashMap<>();

    public void loadKitsFromConfiguration() {
        this.kitConfiguration.getKitList().forEach(kit ->
                this.kitMap.put(kit.getLevel(), kit)
        );
    }

    public Optional<Kit> get(int level) {
        return Optional.ofNullable(this.kitMap.get(level));
    }
}

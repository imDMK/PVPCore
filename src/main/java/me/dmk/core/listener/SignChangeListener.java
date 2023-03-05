package me.dmk.core.listener;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

/**
 * Created by DMK on 05.03.2023
 */

@AllArgsConstructor
public class SignChangeListener implements Listener {

    private final MiniMessage miniMessage;

    private final LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.builder()
            .character('§')
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        String[] lines = event.getLines();

        if (player.hasPermission("core.sign.change")) {
            for (int i = 0; i < lines.length; i++) {
                event.setLine(i,
                        this.legacyComponentSerializer.serialize(this.miniMessage.deserialize(lines[i]))
                );
            }
        }
    }
}

package me.dmk.core.listener;

import lombok.AllArgsConstructor;
import me.dmk.core.profile.cache.ProfileCache;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by DMK on 05.01.2023
 */

@AllArgsConstructor
public class PlayerItemConsumeListener implements Listener {

    private final ProfileCache profileCache;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        ItemStack item = event.getItem();
        Material material = item.getType();

        switch (material) {
            case GOLDEN_APPLE -> this.profileCache.get(player.getUniqueId())
                    .ifPresent(profile -> profile.getProfileStatistics().increaseEatenGoldenApples());

            case ENCHANTED_GOLDEN_APPLE -> this.profileCache.get(player.getUniqueId())
                    .ifPresent(profile -> profile.getProfileStatistics().increaseEatenEnchantedGoldenApples());
        }
    }
}

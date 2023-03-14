package me.dmk.core.listener;

import lombok.AllArgsConstructor;
import me.dmk.core.profile.controller.ProfileController;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by DMK on 12.01.2023
 */

@AllArgsConstructor
public class PlayerInteractListener implements Listener {

    private final ProfileController profileController;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        ItemStack item = event.getItem();
        Material material = event.getMaterial();

        if (item == null) {
            return;
        }

        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
            if (material == Material.GOLDEN_APPLE || material == Material.ENCHANTED_GOLDEN_APPLE) {
                this.profileController.get(player.getUniqueId())
                        .ifPresent(profile -> profile.getProfileStatistics().setLastEatTime(System.currentTimeMillis()));
            }

            if (material == Material.ENDER_PEARL) {
                this.profileController.get(player.getUniqueId())
                        .ifPresent(profile -> profile.getProfileStatistics().increaseThrownEnderPearl());
            }
        }
    }
}

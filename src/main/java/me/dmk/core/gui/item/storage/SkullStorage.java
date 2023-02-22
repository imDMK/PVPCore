package me.dmk.core.gui.item.storage;

import dev.dbassett.skullcreator.SkullCreator;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Created by DMK on 19.01.2023
 */

@UtilityClass
public class SkullStorage {

    public static ItemBuilder createPlayerHead(UUID uuid) {
        return ItemBuilder.from(Material.PLAYER_HEAD)
                .setSkullOwner(Bukkit.getOfflinePlayer(uuid));
    }

    public static ItemBuilder createPlayerHead(OfflinePlayer offlinePlayer) {
        return ItemBuilder.from(Material.PLAYER_HEAD)
                .setSkullOwner(offlinePlayer);
    }

    public static ItemStack createPlayerHeadStack(UUID uuid) {
        return ItemBuilder.from(Material.PLAYER_HEAD)
                .setSkullOwner(Bukkit.getOfflinePlayer(uuid))
                .build();
    }

    public static ItemStack getBlackArrowLeft() {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzdhZWU5YTc1YmYwZGY3ODk3MTgzMDE1Y2NhMGIyYTdkNzU1YzYzMzg4ZmYwMTc1MmQ1ZjQ0MTlmYzY0NSJ9fX0=";

        return SkullCreator.itemFromBase64(base64);
    }

    public static ItemStack getBlackArrowRight() {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgyYWQxYjljYjRkZDIxMjU5YzBkNzVhYTMxNWZmMzg5YzNjZWY3NTJiZTM5NDkzMzgxNjRiYWM4NGE5NmUifX19";

        return SkullCreator.itemFromBase64(base64);
    }

    public static ItemStack getBlackArrowUp() {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNjYmY5ODgzZGQzNTlmZGYyMzg1YzkwYTQ1OWQ3Mzc3NjUzODJlYzQxMTdiMDQ4OTVhYzRkYzRiNjBmYyJ9fX0=";

        return SkullCreator.itemFromBase64(base64);
    }

    public static ItemStack getBlackArrowDown() {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzI0MzE5MTFmNDE3OGI0ZDJiNDEzYWE3ZjVjNzhhZTQ0NDdmZTkyNDY5NDNjMzFkZjMxMTYzYzBlMDQzZTBkNiJ9fX0=";

        return SkullCreator.itemFromBase64(base64);
    }
}

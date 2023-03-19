package me.dmk.core.gui.item.storage;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.builder.item.SkullBuilder;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Created by DMK on 19.01.2023
 */

@UtilityClass
public class SkullStorage {

    private static final String blackArrowLeft = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzdhZWU5YTc1YmYwZGY3ODk3MTgzMDE1Y2NhMGIyYTdkNzU1YzYzMzg4ZmYwMTc1MmQ1ZjQ0MTlmYzY0NSJ9fX0=";
    private static final String blackArrowRight = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgyYWQxYjljYjRkZDIxMjU5YzBkNzVhYTMxNWZmMzg5YzNjZWY3NTJiZTM5NDkzMzgxNjRiYWM4NGE5NmUifX19";
    private static final String blackArrowUp = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNjYmY5ODgzZGQzNTlmZGYyMzg1YzkwYTQ1OWQ3Mzc3NjUzODJlYzQxMTdiMDQ4OTVhYzRkYzRiNjBmYyJ9fX0=";
    private static final String blackArrowDown = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzI0MzE5MTFmNDE3OGI0ZDJiNDEzYWE3ZjVjNzhhZTQ0NDdmZTkyNDY5NDNjMzFkZjMxMTYzYzBlMDQzZTBkNiJ9fX0=";

    public static SkullBuilder createPlayerHead(UUID uuid) {
        return ItemBuilder
                .skull()
                .owner(Bukkit.getOfflinePlayer(uuid));
    }

    public static ItemStack getBlackArrowLeft() {
        return ItemBuilder.skull()
                .texture(blackArrowLeft)
                .build();
    }

    public static ItemStack getBlackArrowRight() {
        return ItemBuilder.skull()
                .texture(blackArrowRight)
                .build();
    }

    public static ItemStack getBlackArrowUp() {
        return ItemBuilder.skull()
                .texture(blackArrowUp)
                .build();
    }

    public static ItemStack getBlackArrowDown() {
        return ItemBuilder.skull()
                .texture(blackArrowDown)
                .build();
    }
}

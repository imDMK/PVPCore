package me.dmk.core.profile.kit;

import eu.okaeri.configs.OkaeriConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by DMK on 21.02.2023
 */

@Getter
@AllArgsConstructor
public class Kit extends OkaeriConfig {

    private String name;
    private List<String> lore;

    private int level;
    private int requiredCoinsToBuy;

    private ItemStack icon;
    private List<ItemStack> items;
}

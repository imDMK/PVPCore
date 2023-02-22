package me.dmk.core.configuration;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import lombok.Getter;
import me.dmk.core.kit.Kit;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

/**
 * Created by DMK on 21.02.2023
 */

@Getter
public class KitConfiguration extends OkaeriConfig {

    @Comment("# Kit list")
    public List<Kit> kitList = Arrays.asList(
            new Kit(
                    "<gray>Podstawowy",
                    List.of(
                            "",
                            "<gray>Podstawowy zestaw ",
                            ""
                    ),
                    1,
                    0,
                    ItemBuilder.from(Material.WOODEN_SWORD).build(),
                    List.of(
                            ItemBuilder.from(Material.DIAMOND_SWORD).build(),
                            ItemBuilder.from(Material.GOLDEN_APPLE).amount(3).build(),
                            ItemBuilder.from(Material.ENCHANTED_GOLDEN_APPLE).amount(2).build()
                    )
            ),
            new Kit(
                    "<yellow>Zaawansowany",
                    List.of(
                            "",
                            "<gray>Zaawansowany zestaw (1000 monet)",
                            ""
                    ),
                    2,
                    1000,
                    ItemBuilder.from(Material.STONE_SWORD).build(),
                    List.of(
                            ItemBuilder.from(Material.NETHERITE_SWORD).build(),
                            ItemBuilder.from(Material.GOLDEN_APPLE).amount(6).build(),
                            ItemBuilder.from(Material.ENCHANTED_GOLDEN_APPLE).amount(3).build()
                    )
            )
    );
}

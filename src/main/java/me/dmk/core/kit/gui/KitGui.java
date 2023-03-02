package me.dmk.core.kit.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.gui.item.builder.BarrierBuilder;
import me.dmk.core.gui.item.storage.SkullStorage;
import me.dmk.core.kit.Kit;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.statistics.ProfileStatistics;
import me.dmk.core.util.ComponentUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by DMK on 21.02.2023
 */

public class KitGui extends PluginGui {

    public void open(Player player, Profile profile) {
        ProfileStatistics statistics = profile.getProfileStatistics();
        int profileKitLevel = statistics.getKitLevel();

        Gui gui = Gui.gui()
                .title(ComponentUtil.text(this.circle + " <light_purple>Zestawy " + this.circle))
                .rows(3)
                .disableAllInteractions()
                .create();

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());

        for (Kit kit : this.kitMap.getKitMap().values()) {
            GuiItem kitItem = ItemBuilder.from(kit.getIcon())
                    .name(ComponentUtil.text(kit.getName()))
                    .lore(ComponentUtil.asList(kit.getLore()))
                    .glow(kit.getLevel() == profileKitLevel)
                    .asGuiItem(event -> new KitPreviewGui().open(player, profile, kit));

            gui.addItem(kitItem);
        }

        GuiItem upgrateKitItem = ItemBuilder.from(SkullStorage.getBlackArrowUp())
                .name(ComponentUtil.text("<yellow>Ulepsz zestaw"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój aktualny poziom zestawu<dark_gray>: <yellow>" + profileKitLevel,
                        this.circle + " <yellow>Kliknij<dark_gray>, <gray>aby <yellow>ulepszyć <gray>swój zestaw<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    Optional<Kit> nextKitOptional = this.kitMap.get(profileKitLevel + 1);

                    if (nextKitOptional.isEmpty()) {
                        new BarrierBuilder()
                                .name("<red>Nie ma następnego zestawu do odblokowania<dark_gray>.")
                                .updateItem(gui, event.getSlot());
                        return;
                    }

                    Kit nextKit = nextKitOptional.get();

                    if (nextKit.getRequiredCoinsToBuy() > statistics.getCoins()) {
                        new BarrierBuilder()
                                .name("<red>Aby ulepszyć zestaw potrzebujesz <gold>" + nextKit.getRequiredCoinsToBuy() + " <red>monet<dark_gray>.")
                                .updateItem(gui, event.getSlot());
                        return;
                    }

                    Bukkit.dispatchCommand(player, "kit upgrade");
                    gui.close(player);
                });

        gui.setItem(22, upgrateKitItem);

        gui.open(player);
    }
}

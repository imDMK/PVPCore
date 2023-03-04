package me.dmk.core.profile.kit.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.CorePlugin;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.gui.item.builder.BarrierBuilder;
import me.dmk.core.gui.item.storage.SkullStorage;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.kit.Kit;
import me.dmk.core.profile.kit.KitMap;
import me.dmk.core.profile.statistics.ProfileStatistics;
import me.dmk.core.util.ComponentUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by DMK on 02.03.2023
 */

public class KitGui extends PluginGui {

    public final KitMap kitMap = CorePlugin.getCorePlugin().getKitMap();

    public KitGui(Player player, Profile profile) {
        super(player, profile, "Zestawy", 3, true, true);
    }

    @Override
    public void build() {
        ProfileStatistics statistics = this.profile.getProfileStatistics();

        int profileKitLevel = statistics.getKitLevel();

        for (Kit kit : this.kitMap.getIntegerKitMap().values()) {
            GuiItem kitItem = ItemBuilder.from(kit.getIcon())
                    .name(ComponentUtil.text(kit.getName()))
                    .lore(ComponentUtil.asList(kit.getLore()))
                    .glow(kit.getLevel() == profileKitLevel)
                    .asGuiItem(event -> new KitPrewiewGui(this.player, this.profile, kit).open());

            this.gui.addItem(kitItem);
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
                                .updateItem(this.gui, event.getSlot());
                        return;
                    }

                    Kit nextKit = nextKitOptional.get();

                    if (nextKit.getRequiredCoinsToBuy() > statistics.getCoins()) {
                        new BarrierBuilder()
                                .name("<red>Aby ulepszyć zestaw potrzebujesz <gold>" + nextKit.getRequiredCoinsToBuy() + " <red>monet<dark_gray>.")
                                .updateItem(this.gui, event.getSlot());
                        return;
                    }

                    Bukkit.dispatchCommand(this.player, "kit upgrade");
                    this.gui.close(this.player);
                });

        this.gui.setItem(22, upgrateKitItem);
    }
}

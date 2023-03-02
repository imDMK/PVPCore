package me.dmk.core.gui.item.builder;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.dmk.core.util.ComponentUtil;
import org.bukkit.Material;

/**
 * Created by DMK on 18.01.2023
 */

public class BarrierBuilder {

    private final ItemBuilder barrier = ItemBuilder.from(Material.BARRIER);

    public BarrierBuilder name(String name) {
        this.barrier.name(
                ComponentUtil.text(name)
        );
        return this;
    }

    public BarrierBuilder lore(String... lore) {
        this.barrier.lore(
                ComponentUtil.asList(lore)
        );
        return this;
    }

    public void updateItem(Gui gui, int slot) {
        gui.updateItem(slot, this.barrier.asGuiItem());
    }

    public void updateItem(PaginatedGui paginatedGui, int slot) {
        paginatedGui.updateItem(slot, this.barrier.asGuiItem());
    }
}

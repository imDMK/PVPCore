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

    private ItemBuilder barrier;

    public BarrierBuilder name(String name) {
        this.barrier = ItemBuilder.from(Material.BARRIER)
                .name(ComponentUtil.text(name));
        return this;
    }

    public BarrierBuilder lore(String... lore) {
        this.barrier = this.barrier
                .lore(ComponentUtil.asList(lore));
        return this;
    }

    public void updateGui(Gui gui, int slot) {
        gui.updateItem(slot, this.barrier.asGuiItem());
    }

    public void updateGui(PaginatedGui paginatedGui, int slot) {
        paginatedGui.updateItem(slot, this.barrier.asGuiItem());
    }
}

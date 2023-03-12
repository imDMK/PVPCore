package me.dmk.core.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.item.storage.ItemStorage;
import me.dmk.core.util.ComponentUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by DMK on 02.03.2023
 */

public abstract class PluginGui extends ItemStorage {

    public final Player player;
    public final Gui gui;

    public PluginGui(Player player, String name, int rows, boolean disableAllInteractions, boolean fillBorder) {
        this.player = player;

        this.gui = Gui.gui()
                .title(ComponentUtil.text(name))
                .rows(rows)
                .create();

        if (disableAllInteractions) {
            this.gui.disableAllInteractions();
        }

        if (fillBorder) {
            this.gui.getFiller().fillBorder(
                    ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem()
            );
        }
    }

    public void updateTitle(String title) {
        this.gui.updateTitle(title);
    }

    public Collection<GuiItem> getItems() {
        return this.gui.getGuiItems().values();
    }

    public abstract void build();

    public void open() {
        this.open(true);
    }

    public void open(boolean build) {
        if (build) {
            this.build();
        }

        this.gui.open(this.player);
    }

    public void close() {
        this.gui.close(this.player);
    }
}

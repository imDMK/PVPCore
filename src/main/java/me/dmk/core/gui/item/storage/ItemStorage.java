package me.dmk.core.gui.item.storage;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.SymbolUtil;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by DMK on 18.01.2023
 */

public class ItemStorage {

    public String circle = SymbolUtil.getCircle("<dark_gray>");
    public String warning = StringFormatter.formatWarning();

    public GuiItem createNextPageButton(PaginatedGui gui) {
        return ItemBuilder.from(SkullStorage.getBlackArrowRight())
                .name(ComponentUtil.text("<light_purple>Następna strona"))
                .lore(ComponentUtil.asList(
                        "",
                        this.warning + " <gold>Kliknij<dark_gray>, <gray>aby przejść na następną stronę<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> gui.next());
    }

    public GuiItem createPreviousPageButton(PaginatedGui gui) {
        return ItemBuilder.from(SkullStorage.getBlackArrowLeft())
                .name(ComponentUtil.text("<light_purple>Poprzednia strona"))
                .lore(ComponentUtil.asList(
                        "",
                        this.warning + " <gold>Kliknij<dark_gray>, <gray>aby przejść na poprzednią stronę<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> gui.previous());
    }

    public GuiItem createBackButton(GuiAction<InventoryClickEvent> action, String... lore) {
        return ItemBuilder.from(SkullStorage.getBlackArrowDown())
                .name(ComponentUtil.text("<light_purple>Powrót"))
                .lore(ComponentUtil.asList(lore))
                .asGuiItem(action);
    }
}

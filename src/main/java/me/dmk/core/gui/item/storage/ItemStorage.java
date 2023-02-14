package me.dmk.core.gui.item.storage;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.experimental.UtilityClass;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.StyleUtil;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by DMK on 18.01.2023
 */

@UtilityClass
public class ItemStorage {

    public static GuiItem createNextPageButton(PaginatedGui gui) {
        return ItemBuilder.from(SkullStorage.getMagentaArrowRight())
                .name(ComponentUtil.text("<light_purple>Następna strona"))
                .lore(ComponentUtil.asList(
                        "",
                        StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść na następną stronę<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    gui.next();
                });
    }

    public static GuiItem createPreviousPageButton(PaginatedGui gui) {
        return ItemBuilder.from(SkullStorage.getMagentaArrowLeft())
                .name(ComponentUtil.text("<light_purple>Poprzednia strona"))
                .lore(ComponentUtil.asList(
                        "",
                        StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść na poprzednią stronę<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    gui.previous();
                });
    }

    public static GuiItem createBackButton(GuiAction<InventoryClickEvent> action, String... lore) {
        return ItemBuilder.from(SkullStorage.getMagentaArrowDown())
                .name(ComponentUtil.text("<light_purple>Powrót"))
                .lore(ComponentUtil.asList(lore))
                .asGuiItem(action);
    }
}

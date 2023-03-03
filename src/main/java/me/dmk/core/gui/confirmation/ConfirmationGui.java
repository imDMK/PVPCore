package me.dmk.core.gui.confirmation;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.RequiredArgsConstructor;
import me.dmk.core.CorePlugin;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.string.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by DMK on 18.01.2023
 */

@RequiredArgsConstructor
public class ConfirmationGui {

    private final Player player;
    private Gui gui;

    private GuiAction<InventoryClickEvent> actionAfterConfirm;
    private GuiAction<InventoryClickEvent> actionAfterCancel;

    private boolean closeAfterCancel;

    public ConfirmationGui create(String title) {
        this.gui = Gui.gui()
                .title(ComponentUtil.text(title))
                .rows(6)
                .disableAllInteractions()
                .create();
        return this;
    }

    public ConfirmationGui afterConfirm(GuiAction<InventoryClickEvent> actionAfterConfirm) {
        this.actionAfterConfirm = actionAfterConfirm;
        return this;
    }

    public ConfirmationGui afterCancel(GuiAction<InventoryClickEvent> actionAfterCancel) {
        this.actionAfterCancel = actionAfterCancel;
        return this;
    }

    public ConfirmationGui closeAfterCancel() {
        this.closeAfterCancel = true;
        return this;
    }

    public void open() {
        this.open(false);
    }

    public void open(boolean async) {
        GuiItem cancelItem = ItemBuilder.from(Material.RED_CONCRETE)
                .name(ComponentUtil.text(StringUtil.getRedGradient() + "Anuluję"))
                .asGuiItem();

        GuiItem confirmItem = ItemBuilder.from(Material.GREEN_CONCRETE)
                .name(ComponentUtil.text(StringUtil.getGreenGradient() + "Potwierdzam"))
                .asGuiItem();

        if (this.actionAfterConfirm != null) {
            confirmItem.setAction(this.actionAfterConfirm);
        }

        if (this.actionAfterCancel != null) {
            cancelItem.setAction(this.actionAfterCancel);
        } else if (this.closeAfterCancel) {
            cancelItem.setAction(event -> this.gui.close(this.player));
        }

        this.gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());

        this.gui.setItem(21, confirmItem);
        this.gui.setItem(30, confirmItem);

        this.gui.setItem(23, cancelItem);
        this.gui.setItem(32, cancelItem);

        if (async) { //opening inventory cannot be async
            Bukkit.getScheduler().runTaskLater(CorePlugin.getCorePlugin(), () -> this.gui.open(this.player), 1L);
        } else {
            this.gui.open(this.player);
        }
    }
}

package me.dmk.core.kit.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.kit.Kit;
import me.dmk.core.profile.Profile;
import me.dmk.core.util.ComponentUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by DMK on 22.02.2023
 */

public class KitPreviewGui extends PluginGui {

    public void open(Player player, Profile profile, Kit kit) {
        Gui gui = Gui.gui()
                .title(ComponentUtil.text(this.circle + " <light_purple>Zestaw " + kit.getName() + " " + this.circle))
                .rows(6)
                .disableAllInteractions()
                .create();

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());

        for (ItemStack kitItem : kit.getItems()) {
            GuiItem guiItem = ItemBuilder.from(kitItem).asGuiItem();

            gui.addItem(guiItem);
        }

        GuiItem backButton = this.createBackButton(event ->
                        new KitGui().open(player, profile),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do listy zestawów<dark_gray>.",
                ""
        );

        gui.setItem(49, backButton);

        gui.open(player);
    }
}

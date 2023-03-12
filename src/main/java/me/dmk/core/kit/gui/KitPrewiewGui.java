package me.dmk.core.kit.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.kit.Kit;
import me.dmk.core.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by DMK on 02.03.2023
 */

public class KitPrewiewGui extends PluginGui {

    private final Profile profile;
    private final Kit kit;

    public KitPrewiewGui(Player player, Profile profile, Kit kit) {
        super(player, "Zestaw " + kit.getName(), 6, true, true);

        this.profile = profile;
        this.kit = kit;
    }

    @Override
    public void build() {
        for (ItemStack kitItem : this.kit.getItems()) {
            GuiItem guiItem = ItemBuilder.from(kitItem).asGuiItem();

            this.gui.addItem(guiItem);
        }

        GuiItem backButton = this.createBackButton(event ->
                        new KitGui(this.player, this.profile).open(),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do listy zestawów<dark_gray>.",
                ""
        );

        this.gui.setItem(49, backButton);
    }
}

package me.dmk.core.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.dmk.core.gui.item.storage.ItemStorage;
import me.dmk.core.profile.Profile;
import me.dmk.core.util.ComponentUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 02.03.2023
 */

public abstract class PluginPaginatedGui extends ItemStorage {

    public final Player player;
    public final Profile profile;

    public final PaginatedGui gui;

    public PluginPaginatedGui(Player player, Profile profile, String name, int rows, boolean disableAllInteractions, boolean fillBorder) {
        this.player = player;
        this.profile = profile;

        this.gui = Gui.paginated()
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

    public boolean isSelf() {
        return this.player.getUniqueId().equals(this.profile.getUuid());
    }

    public abstract void build();

    public void open() {
        this.build();
        this.gui.open(this.player);
    }

    public void close() {
        this.gui.close(this.player);
    }
}

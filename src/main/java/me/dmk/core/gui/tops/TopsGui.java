package me.dmk.core.gui.tops;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.gui.tops.implementation.*;
import me.dmk.core.util.ComponentUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 12.02.2023
 */

public class TopsGui extends PluginGui {

    public void open(Player player) {
        Gui gui = Gui.gui()
                .title(ComponentUtil.text(this.circle + " <light_purple>Topki serwerowe " + this.circle))
                .rows(5)
                .disableAllInteractions()
                .create();

        GuiItem entrancesItem = ItemBuilder.from(Material.DARK_OAK_DOOR)
                .name(ComponentUtil.text("<light_purple>Topka wejść na serwer"))
                .asGuiItem(event -> new EntrancesTopsGui().open(player));

        GuiItem timeSpentItem = ItemBuilder.from(Material.CLOCK)
                .name(ComponentUtil.text("<gold>Topka spędzonego czasu"))
                .asGuiItem(event -> new TimeSpentTopsGui().open(player));

        GuiItem levelItem = ItemBuilder.from(Material.EXPERIENCE_BOTTLE)
                .name(ComponentUtil.text("<yellow>Topka poziomów"))
                .asGuiItem(event -> new LevelTopsGui().open(player));

        GuiItem coinsItem = ItemBuilder.from(Material.SUNFLOWER)
                .name(ComponentUtil.text("<yellow>Topka monet"))
                .asGuiItem(event -> new CoinsTopsGui().open(player));

        GuiItem pointsItem = ItemBuilder.from(Material.NETHER_STAR)
                .name(ComponentUtil.text("<gold>Topka punktów rankingowych"))
                .asGuiItem(event -> new PointsTopsGui().open(player));

        GuiItem killsItem = ItemBuilder.from(Material.DIAMOND_SWORD)
                .name(ComponentUtil.text("<red>Topka zabójstw"))
                .asGuiItem(event -> new KillsTopsGui().open(player));

        GuiItem highestKillStreak = ItemBuilder.from(Material.NETHERITE_SWORD)
                .name(ComponentUtil.text("<red>Topka największej serii zabójstw"))
                .asGuiItem(event -> new KillStreakTopsGui().open(player));

        GuiItem deathsItem = ItemBuilder.from(Material.SKELETON_SKULL)
                .name(ComponentUtil.text("<gray>Topka śmierci"))
                .asGuiItem(event -> new DeathsTopsGui().open(player));

        GuiItem guildsItem = ItemBuilder.from(Material.BEACON)
                .name(ComponentUtil.text("<aqua>Topki gildyjne"))
                .asGuiItem(event -> new GuildsTopsGui().open(player));

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());

        gui.setItem(12, entrancesItem);
        gui.setItem(13, timeSpentItem);
        gui.setItem(14, levelItem);

        gui.setItem(21, coinsItem);
        gui.setItem(22, pointsItem);
        gui.setItem(23, killsItem);

        gui.setItem(30, highestKillStreak);
        gui.setItem(31, deathsItem);
        gui.setItem(32, guildsItem);

        gui.open(player);
    }
}

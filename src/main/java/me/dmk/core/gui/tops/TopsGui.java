package me.dmk.core.gui.tops;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.gui.tops.implementation.CoinsTopsGui;
import me.dmk.core.gui.tops.implementation.DeathsTopsGui;
import me.dmk.core.gui.tops.implementation.EntrancesTopsGui;
import me.dmk.core.gui.tops.implementation.GuildsTopsGui;
import me.dmk.core.gui.tops.implementation.HighestKillStreakTopsGui;
import me.dmk.core.gui.tops.implementation.KillsTopsGui;
import me.dmk.core.gui.tops.implementation.LevelTopsGui;
import me.dmk.core.gui.tops.implementation.PointsTopsGui;
import me.dmk.core.gui.tops.implementation.TimeSpentTopsGui;
import me.dmk.core.util.ComponentUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 02.03.2023
 */

public class TopsGui extends PluginGui {
    public TopsGui(Player player) {
        super(player, "Topki serwerowe", 5, true, true);
    }

    @Override
    public void build() {
        GuiItem entrancesItem = ItemBuilder.from(Material.DARK_OAK_DOOR)
                .name(ComponentUtil.text("<light_purple>Topka wejść na serwer"))
                .asGuiItem(event ->
                        new EntrancesTopsGui(this.player).open()
                );

        GuiItem timeSpentItem = ItemBuilder.from(Material.CLOCK)
                .name(ComponentUtil.text("<gold>Topka spędzonego czasu"))
                .asGuiItem(event ->
                        new TimeSpentTopsGui(this.player).open()
                );

        GuiItem levelItem = ItemBuilder.from(Material.EXPERIENCE_BOTTLE)
                .name(ComponentUtil.text("<yellow>Topka poziomów"))
                .asGuiItem(event ->
                        new LevelTopsGui(this.player).open()
                );

        GuiItem coinsItem = ItemBuilder.from(Material.SUNFLOWER)
                .name(ComponentUtil.text("<yellow>Topka monet"))
                .asGuiItem(event ->
                        new CoinsTopsGui(this.player).open()
                );

        GuiItem pointsItem = ItemBuilder.from(Material.NETHER_STAR)
                .name(ComponentUtil.text("<gold>Topka punktów rankingowych"))
                .asGuiItem(event ->
                        new PointsTopsGui(this.player).open()
                );

        GuiItem killsItem = ItemBuilder.from(Material.DIAMOND_SWORD)
                .name(ComponentUtil.text("<red>Topka zabójstw"))
                .asGuiItem(event ->
                        new KillsTopsGui(this.player).open()
                );

        GuiItem highestKillStreak = ItemBuilder.from(Material.NETHERITE_SWORD)
                .name(ComponentUtil.text("<red>Topka największej serii zabójstw"))
                .asGuiItem(event ->
                        new HighestKillStreakTopsGui(this.player).open()
                );

        GuiItem deathsItem = ItemBuilder.from(Material.SKELETON_SKULL)
                .name(ComponentUtil.text("<gray>Topka śmierci"))
                .asGuiItem(event ->
                        new DeathsTopsGui(this.player).open()
                );

        GuiItem guildsItem = ItemBuilder.from(Material.BEACON)
                .name(ComponentUtil.text("<aqua>Topki gildyjne"))
                .asGuiItem(event ->
                        new GuildsTopsGui(this.player).open()
                );

        this.gui.setItem(12, entrancesItem);
        this.gui.setItem(13, timeSpentItem);
        this.gui.setItem(14, levelItem);

        this.gui.setItem(21, coinsItem);
        this.gui.setItem(22, pointsItem);
        this.gui.setItem(23, killsItem);

        this.gui.setItem(30, highestKillStreak);
        this.gui.setItem(31, deathsItem);
        this.gui.setItem(32, guildsItem);
    }
}

package me.dmk.core.gui.tops;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.AllArgsConstructor;
import me.dmk.core.gui.tops.implementation.*;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.StyleUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 12.02.2023
 */

@AllArgsConstructor
public class TopsGui {

    private final ProfileController profileController;
    private final GuildController guildController;

    public void open(Player player) {
        String circle = StyleUtil.getCircle();
        String purleGradient = StyleUtil.getPurpleGradient();

        Gui gui = Gui.gui()
                .title(ComponentUtil.text(circle + " <light_purple>Topki serwerowe " + circle))
                .rows(5)
                .disableAllInteractions()
                .create();

        GuiItem entrancesItem = ItemBuilder.from(Material.DARK_OAK_DOOR)
                .name(ComponentUtil.text(purleGradient + "Topka wejść na serwer"))
                .asGuiItem(event -> new EntrancesTopsGui(this.profileController, this.guildController).open(player));

        GuiItem timeSpentItem = ItemBuilder.from(Material.CLOCK)
                .name(ComponentUtil.text(purleGradient + "Topka spędzonego czasu"))
                .asGuiItem(event -> new TimeSpentTopsGui(this.profileController, this.guildController).open(player));

        GuiItem levelItem = ItemBuilder.from(Material.EXPERIENCE_BOTTLE)
                .name(ComponentUtil.text(purleGradient + "Topka poziomów"))
                .asGuiItem(event -> new LevelTopsGui(this.profileController, this.guildController).open(player));

        GuiItem coinsItem = ItemBuilder.from(Material.SUNFLOWER)
                .name(ComponentUtil.text(purleGradient + "Topka monet"))
                .asGuiItem(event -> new CoinsTopsGui(this.profileController, this.guildController).open(player));

        GuiItem pointsItem = ItemBuilder.from(Material.NETHER_STAR)
                .name(ComponentUtil.text(purleGradient + "Topka punktów rankingowych"))
                .asGuiItem(event -> new PointsTopsGui(this.profileController, this.guildController).open(player));

        GuiItem killsItem = ItemBuilder.from(Material.DIAMOND_SWORD)
                .name(ComponentUtil.text(purleGradient + "Topka zabójstw"))
                .asGuiItem(event -> new KillsTopsGui(this.profileController, this.guildController).open(player));

        GuiItem highestKillStreak = ItemBuilder.from(Material.NETHERITE_SWORD)
                .name(ComponentUtil.text(purleGradient + "Topka największej serii zabójstw"))
                .asGuiItem(event -> new KillStreakTopsGui(this.profileController, this.guildController).open(player));

        GuiItem deathsItem = ItemBuilder.from(Material.SKELETON_SKULL)
                .name(ComponentUtil.text(purleGradient + "Topka śmierci"))
                .asGuiItem(event -> new DeathsTopsGui(this.profileController, this.guildController).open(player));

        GuiItem guildsItem = ItemBuilder.from(Material.BEACON)
                .name(ComponentUtil.text(purleGradient + "Topki gildyjne"))
                .asGuiItem(event -> new GuildsTopsGui(this.profileController, this.guildController).open(player));

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

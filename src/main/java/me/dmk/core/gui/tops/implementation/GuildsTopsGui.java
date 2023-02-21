package me.dmk.core.gui.tops.implementation;

import com.mongodb.client.model.Sorts;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.AllArgsConstructor;
import me.dmk.core.gui.item.storage.ItemStorage;
import me.dmk.core.gui.tops.TopsGui;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.guild.statistics.GuildStatistics;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.string.SymbolUtil;
import org.bson.conversions.Bson;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by DMK on 13.02.2023
 */

@AllArgsConstructor
public class GuildsTopsGui extends ItemStorage {

    private final ProfileController profileController;
    private final GuildController guildController;

    public void open(Player player) {
        Gui gui = Gui.gui()
                .title(ComponentUtil.text(this.circle + " <light_purple>Topki serwerowe " + this.circle))
                .rows(5)
                .disableAllInteractions()
                .create();

        GuiItem backButton = this.createBackButton(event ->
                        new TopsGui(this.profileController, this.guildController).open(player),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do menu topek<dark_gray>.",
                ""
        );

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());
        gui.setItem(31, backButton);

        Bson sort = Sorts.descending("guildStatistics.rank");
        List<Guild> guildList = this.guildController.getTops(sort, 14);

        for (int i = 0; i < guildList.size(); i++) {
            Guild guild = guildList.get(i);
            GuildStatistics statistics = guild.getGuildStatistics();

            GuiItem item = ItemBuilder.from(Material.BEACON)
                    .name(ComponentUtil.text((i + 1) + ". " + guild.getTag()))
                    .lore(ComponentUtil.asList(
                            "",
                            this.warning + " <gray>Statystyki gildii<dark_gray>:",
                            SymbolUtil.getStar("<gold>") + " <gray>Ranking<dark_gray>: <gold>" + statistics.getRank(),
                            SymbolUtil.getSword("<red>") + " <gray>Zabójstwa<dark_gray>: <red>" + statistics.getKills(),
                            SymbolUtil.getSword("<red>") + " <gray>Seria zabójstw<dark_gray>: <red>" + statistics.getKillStreak(),
                            SymbolUtil.getSword("<red>") + " <gray>Największa seria zabójstw<dark_gray>: <red>" + statistics.getHighestKillStreak(),
                            SymbolUtil.getDeath("<gray>") + " <gray>Śmierci<dark_gray>: <gray>" + statistics.getDeaths(),
                            ""
                    ))
                    .asGuiItem();

            gui.addItem(item);
        }

        gui.open(player);
    }
}

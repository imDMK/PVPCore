package me.dmk.core.gui.tops.implementation;

import com.mongodb.client.model.Indexes;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.CorePlugin;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.gui.tops.TopsGui;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.guild.statistics.GuildStatistics;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.string.SymbolUtil;
import org.bson.conversions.Bson;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by DMK on 02.03.2023
 */

public class GuildsTopsGui extends PluginGui {

    private final GuildController guildController = CorePlugin.getCorePlugin().getGuildController();

    public GuildsTopsGui(Player player) {
        super(player, "Topka gildii", 5, true, true);
    }

    @Override
    public void build() {
        GuiItem backButton = this.createBackButton(event ->
                        new TopsGui(this.player).open(),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do menu topek<dark_gray>.",
                ""
        );

        this.gui.setItem(31, backButton);

        Bson sort = Indexes.descending("guildStatistics.rank");
        List<Guild> profileList = this.guildController.getTops(sort, 14);

        for (int i = 0; i < profileList.size(); i++) {
            Guild guild = profileList.get(i);
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

            this.gui.addItem(item);
        }
    }
}

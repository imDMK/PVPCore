package me.dmk.core.gui.tops.implementation;

import com.mongodb.client.model.Indexes;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.AllArgsConstructor;
import me.dmk.core.gui.item.storage.ItemStorage;
import me.dmk.core.gui.item.storage.SkullStorage;
import me.dmk.core.gui.tops.TopsGui;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.statistics.ProfileStatistics;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.TimeUtil;
import me.dmk.core.util.string.SymbolUtil;
import org.bson.conversions.Bson;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;

/**
 * Created by DMK on 13.02.2023
 */

@AllArgsConstructor
public class TimeSpentTopsGui extends ItemStorage {

    private final ProfileController profileController;
    private final GuildController guildController;

    public void open(Player player) {
        Gui gui = Gui.gui()
                .title(ComponentUtil.text(this.circle + " <light_purple>Topka spędzonego czasu " + this.circle))
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

        Bson sort = Indexes.descending("profileStatistics.timeSpent");
        List<Profile> profileList = this.profileController.getTops(sort, 14);

        for (int i = 0; i < profileList.size(); i++) {
            Profile profile = profileList.get(i);
            ProfileStatistics statistics = profile.getProfileStatistics();

            Duration timeSpent = Duration.ofSeconds(statistics.getTimeSpent());

            GuiItem item = SkullStorage.createPlayerHead(profile.getUuid())
                    .name(ComponentUtil.text((i + 1) + ". " + profile.getColoredName()))
                    .lore(ComponentUtil.asList(
                            "",
                            "<gold>" + SymbolUtil.getWatch() + " <gray>Gracz spędził <gold>" + TimeUtil.durationToString(timeSpent) + " <gray>na naszym serwerze<dark_gray>.",
                            ""
                    ))
                    .asGuiItem();

            gui.addItem(item);
        }

        gui.open(player);
    }
}

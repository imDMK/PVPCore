package me.dmk.core.gui.tops.implementation;

import com.mongodb.client.model.Indexes;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.CorePlugin;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.gui.item.storage.SkullStorage;
import me.dmk.core.gui.tops.TopsGui;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.statistics.ProfileStatistics;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.TimeUtil;
import me.dmk.core.util.string.SymbolUtil;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;

/**
 * Created by DMK on 02.03.2023
 */

public class TimeSpentTopsGui extends PluginGui {

    public final ProfileController profileController = CorePlugin.getCorePlugin().getProfileController();

    public TimeSpentTopsGui(Player player) {
        super(player, null, "Topka spędzonego czasu", 5, true, true);
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

        Bson sort = Indexes.descending("profileStatistics.timeSpent");
        List<Profile> profileList = this.profileController.getTops(sort, 14);

        for (int i = 0; i < profileList.size(); i++) {
            Profile profile = profileList.get(i);
            ProfileStatistics statistics = profile.getProfileStatistics();

            String timeSpent = TimeUtil.durationToString(
                    Duration.ofSeconds(statistics.getTimeSpent())
            );

            GuiItem item = SkullStorage.createPlayerHead(profile.getUuid())
                    .name(ComponentUtil.text((i + 1) + ". " + profile.getColoredName()))
                    .lore(ComponentUtil.asList(
                            "",
                            SymbolUtil.getWatch("<gold>") + " <gray>Gracz spędził <gold>" + timeSpent + " <gray>na naszym serwerze<dark_gray>.",
                            ""
                    ))
                    .asGuiItem();

            this.gui.addItem(item);
        }
    }
}

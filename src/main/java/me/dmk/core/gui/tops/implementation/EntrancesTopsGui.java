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
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by DMK on 02.03.2023
 */

public class EntrancesTopsGui extends PluginGui {

    public final ProfileController profileController = CorePlugin.getCorePlugin().getProfileController();

    public EntrancesTopsGui(Player player) {
        super(player, "Topka wejść na serwer", 5, true, true);
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

        Bson sort = Indexes.descending("profileStatistics.entrances");
        List<Profile> profileList = this.profileController.getTops(sort, 14);

        for (int i = 0; i < profileList.size(); i++) {
            Profile profile = profileList.get(i);
            ProfileStatistics statistics = profile.getProfileStatistics();

            GuiItem item = SkullStorage.createPlayerHead(profile.getUuid())
                    .name(ComponentUtil.text((i + 1) + ". " + profile.getColoredName()))
                    .lore(ComponentUtil.asList(
                            "",
                            this.circle + " <gray>Gracz posiada <light_purple>" + statistics.getEntrances() + " <gray>wejść na serwer<dark_gray>.",
                            ""
                    ))
                    .asGuiItem();

            this.gui.addItem(item);
        }
    }
}

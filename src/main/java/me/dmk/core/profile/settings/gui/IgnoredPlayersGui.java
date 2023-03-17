package me.dmk.core.profile.settings.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginPaginatedGui;
import me.dmk.core.gui.item.storage.SkullStorage;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

/**
 * Created by DMK on 02.03.2023
 */

public class IgnoredPlayersGui extends PluginPaginatedGui {

    private final Profile profile;

    public IgnoredPlayersGui(Player player, Profile profile) {
        super(player, "Lista ignorowanych graczy", 6, true, true);

        this.profile = profile;
    }

    @Override
    public void build() {
        ProfileSettings profileSettings = this.profile.getProfileSettings();

        GuiItem previousButton = this.createPreviousPageButton(this.gui);
        GuiItem backButton = this.createBackButton(event ->
                        new ProfileSettingsGui(this.player, this.profile).open(),
                "",
                this.warning + " <gold>Kliknij<dark_gray>, <gray>aby powrócić do ustawień<dark_gray>.",
                ""
        );
        GuiItem nextButton = this.createNextPageButton(this.gui);

        this.gui.setItem(47, previousButton);
        this.gui.setItem(49, backButton);
        this.gui.setItem(51, nextButton);

        for (UUID uuid : profileSettings.getIgnoredPlayers()) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

            GuiItem ignoredPlayer = SkullStorage.createPlayerHead(uuid)
                    .name(ComponentUtil.text("<light_purple>" + offlinePlayer.getName()))
                    .lore(ComponentUtil.asList(
                            "",
                            this.warning + " <gold>Kliknij<dark_gray>, <gray>aby <green>odblokować <gray>tego gracza<dark_gray>.",
                            ""
                    ))
                    .asGuiItem(event -> {
                        profileSettings.getIgnoredPlayers().remove(uuid);

                        this.gui.updateItem(event.getSlot(), ItemBuilder.from(Objects.requireNonNull(event.getCurrentItem()))
                                .lore(ComponentUtil.asList(
                                        "",
                                        StringFormatter.formatSuccess() + " <green>Odblokowano<dark_gray>.",
                                        ""
                                ))
                                .asGuiItem()
                        );
                    });

            this.gui.addItem(ignoredPlayer);
        }
    }
}

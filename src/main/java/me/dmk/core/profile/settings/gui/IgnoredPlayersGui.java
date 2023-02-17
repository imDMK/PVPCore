package me.dmk.core.profile.settings.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.AllArgsConstructor;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.gui.item.storage.ItemStorage;
import me.dmk.core.gui.item.storage.SkullStorage;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

/**
 * Created by DMK on 19.01.2023
 */

@AllArgsConstructor
public class IgnoredPlayersGui extends ItemStorage {

    private final PluginConfiguration pluginConfiguration;
    private final LuckPermsController luckPermsController;
    private final ProfileController profileController;
    private final ProfileCache profileCache;
    private final GuildCache guildCache;

    public void open(Player player, Profile profile) {
        PaginatedGui gui = Gui.paginated()
                .title(ComponentUtil.text(this.circle + " <light_purple>Lista ignorowanych graczy " + this.circle))
                .rows(6)
                .disableAllInteractions()
                .create();

        ProfileSettings profileSettings = profile.getProfileSettings();

        GuiItem previousButton = this.createPreviousPageButton(gui);
        GuiItem backButton = this.createBackButton(event ->
                        new ProfileSettingsGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do ustawień<dark_gray>.",
                ""
        );
        GuiItem nextButton = this.createNextPageButton(gui);

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());

        gui.setItem(47, previousButton);
        gui.setItem(49, backButton);
        gui.setItem(51, nextButton);

        for (UUID uuid : profileSettings.getIgnoredPlayers()) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

            GuiItem ignoredPlayer = ItemBuilder.from(SkullStorage.createPlayerHeadStack(uuid))
                    .name(ComponentUtil.text("<light_purple>" + offlinePlayer.getName()))
                    .lore(ComponentUtil.asList(
                            "",
                            this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby <green>odblokować <gray>tego gracza<dark_gray>.",
                            ""
                    ))
                    .asGuiItem(event -> {
                        profileSettings.getIgnoredPlayers().remove(uuid);

                        gui.updateItem(event.getSlot(), ItemBuilder.from(Objects.requireNonNull(event.getCurrentItem()))
                                .lore(ComponentUtil.asList(
                                        "",
                                        StringFormatter.formatSuccess() + " <green>Odblokowano<dark_gray>.",
                                        ""
                                ))
                                .asGuiItem()
                        );
                    });

            gui.addItem(ignoredPlayer);
        }

        gui.open(player);
    }
}

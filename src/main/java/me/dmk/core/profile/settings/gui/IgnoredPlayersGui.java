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
import me.dmk.core.util.StyleUtil;
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
public class IgnoredPlayersGui {

    private final PluginConfiguration pluginConfiguration;
    private final LuckPermsController luckPermsController;
    private final ProfileController profileController;
    private final ProfileCache profileCache;
    private final GuildCache guildCache;

    public void open(Player player, Profile profile) {
        String circle = StyleUtil.getCircle();

        PaginatedGui gui = Gui.paginated()
                .title(ComponentUtil.text(circle + " <light_purple>Lista ignorowanych graczy " + circle))
                .rows(6)
                .disableAllInteractions()
                .create();

        ProfileSettings profileSettings = profile.getProfileSettings();

        GuiItem previousButton = ItemStorage.createPreviousPageButton(gui);
        GuiItem backButton = ItemStorage.createBackButton(event ->
                        new ProfileSettingsGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile),
                "",
                StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do ustawień<dark_gray>.",
                ""
        );
        GuiItem nextButton = ItemStorage.createNextPageButton(gui);

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
                            StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby <green>odblokować <gray>tego gracza<dark_gray>.",
                            ""
                    ))
                    .asGuiItem(event -> {
                        profileSettings.getIgnoredPlayers().remove(uuid);

                        gui.updateItem(event.getSlot(), ItemBuilder.from(Objects.requireNonNull(event.getCurrentItem()))
                                .lore(ComponentUtil.asList(
                                        "",
                                        StyleUtil.getSuccess() + " <green>Odblokowano<dark_gray>.",
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

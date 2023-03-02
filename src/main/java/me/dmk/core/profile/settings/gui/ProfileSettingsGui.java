package me.dmk.core.profile.settings.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginPaginatedGui;
import me.dmk.core.gui.item.builder.BarrierBuilder;
import me.dmk.core.gui.item.storage.SkullStorage;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.gui.ProfilePanelGui;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.profile.settings.board.Board;
import me.dmk.core.profile.settings.incognito.IncognitoSettings;
import me.dmk.core.profile.settings.nametag.ColorNameType;
import me.dmk.core.profile.settings.nametag.CustomSuffixType;
import me.dmk.core.profile.settings.nametag.gui.NameTagSettingsGui;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 02.03.2023
 */

public class ProfileSettingsGui extends PluginPaginatedGui {
    public ProfileSettingsGui(Player player, Profile profile) {
        super(player, profile, "Ustawienia", 6, true, true);
    }

    @Override
    public void build() {
        ProfileSettings profileSettings = this.profile.getProfileSettings();

        Board board = profileSettings.getBoard();
        IncognitoSettings incognitoSettings = profileSettings.getIncognitoSettings();
        ColorNameType colorNameType = profileSettings.getColorName();
        CustomSuffixType customSuffixType = profileSettings.getCustomSuffix();

        GuiItem sidebarItem = ItemBuilder.from(Material.PAINTING)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Boczny panel"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualny status<dark_gray>: " + StringFormatter.formatBoolean(board.isEnabled()),
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić status<dark_gray>.",
                        ""
                ))
                .glow(board.isEnabled())
                .asGuiItem(event -> {
                    Bukkit.dispatchCommand(this.player, "sidebar");
                    this.open();
                });

        GuiItem soundsItem = ItemBuilder.from(Material.NOTE_BLOCK)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Dźwięki"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualny status<dark_gray>: " + StringFormatter.formatBoolean(profileSettings.isSounds()),
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić status<dark_gray>.",
                        ""
                ))
                .glow(profileSettings.isSounds())
                .asGuiItem(event -> {
                    profileSettings.setSounds(!profileSettings.isSounds());
                    this.open();
                });

        GuiItem nameTagItem = ItemBuilder.from(Material.NAME_TAG)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Zmiana nametagu"))
                .lore(ComponentUtil.asList(
                        "",
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do menu zmiany nametagu<dark_gray>.",
                        ""
                ))
                .glow(colorNameType != ColorNameType.DEAFULT || customSuffixType != CustomSuffixType.NONE)
                .asGuiItem(event ->
                        new NameTagSettingsGui(this.player, this.profile).open()
                );

        GuiItem incognitoItem =
                (incognitoSettings.isEnabled() ?
                        ItemBuilder.from(Material.WITHER_SKELETON_SKULL) :
                        SkullStorage.createPlayerHead(this.profile.getUuid())
                )
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Tryb anonimowy"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualny status<dark_gray>: " + StringFormatter.formatBoolean(incognitoSettings.isEnabled()),
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić status<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    if (!player.hasPermission("core.command.incognito")) {
                        new BarrierBuilder()
                                .name("<red>Nie posiadasz dostępu do tej funkcji")
                                .updateItem(this.gui, event.getSlot());
                        return;
                    }

                    Bukkit.dispatchCommand(this.player, "incognito");
                });

        GuiItem messagesItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Wiadomości"))
                .lore(ComponentUtil.asList(
                        "",
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do menu ustawień wiadomości<dark_gray>.",
                        ""
                ))
                .asGuiItem(event ->
                        new MessagesSettingsGui(this.player, this.profile).open()
                );

        GuiItem ignoredPlayersItem = ItemBuilder.from(Material.RED_DYE)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Zablokowani gracze"))
                .lore(ComponentUtil.asList(
                        "",
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do menu listy ignorowanych graczy<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    if (profileSettings.getIgnoredPlayers().isEmpty()) {
                        new BarrierBuilder()
                                .name("<red>Nikogo nie ignorujesz")
                                .updateItem(gui, event.getSlot());
                        return;
                    }

                    new IgnoredPlayersGui(this.player, this.profile).open();
                });

        GuiItem backButton = this.createBackButton(event ->
                        new ProfilePanelGui(this.player, this.profile).open(),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do panelu gracza<dark_gray>.",
                ""
        );

        this.gui.setItem(21, sidebarItem);
        this.gui.setItem(22, soundsItem);
        this.gui.setItem(23, nameTagItem);

        this.gui.setItem(30, incognitoItem);
        this.gui.setItem(31, messagesItem);
        this.gui.setItem(32, ignoredPlayersItem);

        this.gui.setItem(49, backButton);
    }
}

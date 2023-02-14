package me.dmk.core.profile.settings.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.AllArgsConstructor;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.gui.item.storage.ItemStorage;
import me.dmk.core.gui.item.storage.SkullStorage;
import me.dmk.core.gui.item.builder.BarrierBuilder;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.gui.ProfilePanelGui;
import me.dmk.core.profile.settings.board.Board;
import me.dmk.core.profile.settings.incognito.IncognitoSettings;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.settings.nametag.gui.NameTagSettingsGui;
import me.dmk.core.profile.settings.nametag.ColorNameType;
import me.dmk.core.profile.settings.nametag.CustomSuffixType;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.StyleUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by DMK on 19.01.2023
 */

@AllArgsConstructor
public class ProfileSettingsGui {

    private final PluginConfiguration pluginConfiguration;
    private final LuckPermsController luckPermsController;
    private final ProfileController profileController;
    private final ProfileCache profileCache;
    private final GuildCache guildCache;

    public void open(Player player, Profile profile) {
        String circle = StyleUtil.getCircle();
        String purpleGradient = StyleUtil.getPurpleGradient();

        Gui gui = Gui.gui()
                .title(ComponentUtil.text(circle + " <light_purple>Ustawienia " + circle))
                .rows(6)
                .disableAllInteractions()
                .create();

        ProfileSettings profileSettings = profile.getProfileSettings();

        Board board = profileSettings.getBoard();
        IncognitoSettings incognitoSettings = profileSettings.getIncognitoSettings();
        ColorNameType colorNameType = profileSettings.getColorName();
        CustomSuffixType customSuffixType = profileSettings.getCustomSuffix();

        GuiItem sidebarItem = ItemBuilder.from(Material.PAINTING)
                .name(ComponentUtil.text(purpleGradient + "Boczny panel"))
                .lore(ComponentUtil.asList(
                        "",
                        circle + " <gray>Aktualny status<dark_gray>: " + StyleUtil.formatBoolean(board.isEnabled()),
                        StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić status<dark_gray>.",
                        ""
                ))
                .glow(board.isEnabled())
                .asGuiItem(event -> {
                    Bukkit.dispatchCommand(player, "sidebar");
                    this.open(player, profile);
                });

        GuiItem soundsItem = ItemBuilder.from(Material.NOTE_BLOCK)
                .name(ComponentUtil.text(purpleGradient + "Dźwięki"))
                .lore(ComponentUtil.asList(
                        "",
                        circle + " <gray>Aktualny status<dark_gray>: " + StyleUtil.formatBoolean(profileSettings.isSounds()),
                        StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić status<dark_gray>.",
                        ""
                ))
                .glow(profileSettings.isSounds())
                .asGuiItem(event -> {
                    profileSettings.setSounds(!profileSettings.isSounds());
                    this.open(player, profile);
                });

        GuiItem nameTagItem = ItemBuilder.from(Material.NAME_TAG)
                .name(ComponentUtil.text(purpleGradient + "Zmiana nametagu"))
                .lore(ComponentUtil.asList(
                        "",
                        StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do menu zmiany nametagu<dark_gray>.",
                        ""
                ))
                .glow(colorNameType != ColorNameType.DEAFULT || customSuffixType != CustomSuffixType.NONE)
                .asGuiItem(event ->
                        new NameTagSettingsGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile)
                );

        ItemStack incognitoHead = incognitoSettings.isEnabled() ? new ItemStack(Material.WITHER_SKELETON_SKULL) : SkullStorage.createPlayerHeadStack(profile.getUuid());
        GuiItem incognitoItem = ItemBuilder.from(incognitoHead)
                .name(ComponentUtil.text(purpleGradient + "Tryb anonimowy"))
                .lore(ComponentUtil.asList(
                        "",
                        circle + " <gray>Aktualny status<dark_gray>: " + StyleUtil.formatBoolean(incognitoSettings.isEnabled()),
                        StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić status<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    if (!player.hasPermission("core.command.incognito")) {
                        new BarrierBuilder()
                                .name("<red>Nie posiadasz dostępu do tej funkcji")
                                .updateGui(gui, event.getSlot());
                        return;
                    }

                    Bukkit.dispatchCommand(player, "incognito");
                });

        GuiItem messagesItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(purpleGradient + "Wiadomości"))
                .lore(ComponentUtil.asList(
                        "",
                        StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do menu ustawień wiadomości<dark_gray>.",
                        ""
                ))
                .asGuiItem(event ->
                        new MessagesSettingsGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile)
                );

        GuiItem ignoredPlayersItem = ItemBuilder.from(Material.RED_DYE)
                .name(ComponentUtil.text(purpleGradient + "Zablokowani gracze"))
                .lore(ComponentUtil.asList(
                        "",
                        StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do menu listy ignorowanych graczy<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    if (profileSettings.getIgnoredPlayers().isEmpty()) {
                        new BarrierBuilder()
                                .name("<red>Nikogo nie ignorujesz")
                                .updateGui(gui, event.getSlot());
                        return;
                    }

                    new IgnoredPlayersGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile);
                });

        GuiItem backButton = ItemStorage.createBackButton(event ->
                        new ProfilePanelGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile),
                "",
                StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do panelu gracza<dark_gray>.",
                ""
        );

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());

        gui.setItem(21, sidebarItem);
        gui.setItem(22, soundsItem);
        gui.setItem(23, nameTagItem);

        gui.setItem(30, incognitoItem);
        gui.setItem(31, messagesItem);
        gui.setItem(32, ignoredPlayersItem);

        gui.setItem(49, backButton);

        gui.open(player);
    }
}

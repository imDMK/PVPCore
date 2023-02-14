package me.dmk.core.profile.settings.nametag.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.AllArgsConstructor;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.gui.item.storage.ItemStorage;
import me.dmk.core.gui.item.builder.BarrierBuilder;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.profile.settings.gui.ProfileSettingsGui;
import me.dmk.core.profile.settings.nametag.ColorNameType;
import me.dmk.core.profile.settings.nametag.CustomSuffixType;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.StyleUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 19.01.2023
 */

@AllArgsConstructor
public class NameTagSettingsGui {

    private final PluginConfiguration pluginConfiguration;
    private final LuckPermsController luckPermsController;
    private final ProfileController profileController;
    private final ProfileCache profileCache;
    private final GuildCache guildCache;

    public void open(Player player, Profile profile) {
        String circle = StyleUtil.getCircle();
        String purpleGradient = StyleUtil.getPurpleGradient();

        ProfileSettings profileSettings = profile.getProfileSettings();

        ColorNameType colorNameType = profileSettings.getColorName();
        CustomSuffixType customSuffixType = profileSettings.getCustomSuffix();

        Gui gui = Gui.gui()
                .title(ComponentUtil.text(circle + " <light_purple>Zmiana nametagu " + circle))
                .rows(3)
                .disableAllInteractions()
                .create();

        GuiItem colorNameItem = ItemBuilder.from(Material.RED_DYE)
                .name(ComponentUtil.text(purpleGradient + "Zmiana koloru nicku"))
                .lore(ComponentUtil.asList(
                        "",
                        circle + " <gray>Aktualny kolor nicku<dark_gray>:",
                        circle + " " + colorNameType.getFormat() + profile.getName(),
                        "",
                        StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić kolor nicku",
                        ""
                ))
                .asGuiItem(event -> {
                    if (!player.hasPermission("core.nametag.colorname")) {
                        new BarrierBuilder()
                                .name("<red>Nie posiadasz dostępu do tej funkcji")
                                .lore(
                                        "",
                                        circle + " <gray>Uprawnienie<dark_gray>: <red>core.nametag.colorname",
                                        ""
                                )
                                .updateGui(gui, event.getSlot());
                        return;
                    }

                    new ColorNameGui(this.pluginConfiguration, this.luckPermsController, this.profileController,  this.profileCache, this.guildCache)
                            .open(player, profile);
                });

        GuiItem customSuffixItem = ItemBuilder.from(Material.RED_DYE)
                .name(ComponentUtil.text(purpleGradient + "Zmiana suffixu"))
                .lore(ComponentUtil.asList(
                        "",
                        circle + " <gray>Aktualny suffix<dark_gray>:",
                        circle + " " + profileSettings.getColorName().getFormat() + profile.getName() + " " + customSuffixType.getFormat(),
                        "",
                        StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić suffix",
                        ""
                ))
                .asGuiItem(event -> {
                    if (!player.hasPermission("core.nametag.suffix")) {
                        new BarrierBuilder()
                                .name("<red>Nie posiadasz dostępu do tej funkcji")
                                .lore(
                                        "",
                                        circle + " <gray>Uprawnienie<dark_gray>: <red>core.nametag.suffix",
                                        ""
                                )
                                .updateGui(gui, event.getSlot());
                        return;
                    }

                    new CustomSuffixGui(this.pluginConfiguration, this.luckPermsController, this.profileController,  this.profileCache, this.guildCache)
                            .open(player, profile);
                });

        GuiItem backButton = ItemStorage.createBackButton(event ->
                        new ProfileSettingsGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile),
                "",
                circle + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do menu ustawień<dark_gray>.",
                ""
        );

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());

        gui.setItem(12, colorNameItem);
        gui.setItem(14, customSuffixItem);

        gui.setItem(22, backButton);

        gui.open(player);
    }
}

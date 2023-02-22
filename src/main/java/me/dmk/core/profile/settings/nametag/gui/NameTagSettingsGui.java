package me.dmk.core.profile.settings.nametag.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.gui.item.builder.BarrierBuilder;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.profile.settings.gui.ProfileSettingsGui;
import me.dmk.core.profile.settings.nametag.ColorNameType;
import me.dmk.core.profile.settings.nametag.CustomSuffixType;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.string.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 19.01.2023
 */

public class NameTagSettingsGui extends PluginGui {

    public void open(Player player, Profile profile) {
        ProfileSettings profileSettings = profile.getProfileSettings();

        ColorNameType colorNameType = profileSettings.getColorName();
        CustomSuffixType customSuffixType = profileSettings.getCustomSuffix();

        Gui gui = Gui.gui()
                .title(ComponentUtil.text(this.circle + " <light_purple>Zmiana nametagu " + this.circle))
                .rows(3)
                .disableAllInteractions()
                .create();

        GuiItem colorNameItem = ItemBuilder.from(Material.RED_DYE)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Zmiana koloru nicku"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualny kolor nicku<dark_gray>:",
                        this.circle + " " + colorNameType.getFormat() + profile.getName(),
                        "",
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić kolor nicku",
                        ""
                ))
                .asGuiItem(event -> {
                    if (!player.hasPermission("core.nametag.colorname")) {
                        new BarrierBuilder()
                                .name("<red>Nie posiadasz dostępu do tej funkcji")
                                .lore(
                                        "",
                                        this.circle + " <gray>Uprawnienie<dark_gray>: <red>core.nametag.colorname",
                                        ""
                                )
                                .updateGui(gui, event.getSlot());
                        return;
                    }

                    new ColorNameGui()
                            .open(player, profile);
                });

        GuiItem customSuffixItem = ItemBuilder.from(Material.RED_DYE)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Zmiana suffixu"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualny suffix<dark_gray>:",
                        this.circle + " " + profileSettings.getColorName().getFormat() + profile.getName() + " " + customSuffixType.getFormat(),
                        "",
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić suffix",
                        ""
                ))
                .asGuiItem(event -> {
                    if (!player.hasPermission("core.nametag.suffix")) {
                        new BarrierBuilder()
                                .name("<red>Nie posiadasz dostępu do tej funkcji")
                                .lore(
                                        "",
                                        this.circle + " <gray>Uprawnienie<dark_gray>: <red>core.nametag.suffix",
                                        ""
                                )
                                .updateGui(gui, event.getSlot());
                        return;
                    }

                    new CustomSuffixGui()
                            .open(player, profile);
                });

        GuiItem backButton = this.createBackButton(event ->
                        new ProfileSettingsGui().open(player, profile),
                "",
                this.circle + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do menu ustawień<dark_gray>.",
                ""
        );

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());

        gui.setItem(12, colorNameItem);
        gui.setItem(14, customSuffixItem);

        gui.setItem(22, backButton);

        gui.open(player);
    }
}

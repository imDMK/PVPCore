package me.dmk.core.profile.settings.nametag.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.gui.item.builder.BarrierBuilder;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.profile.settings.gui.ProfileSettingsGui;
import me.dmk.core.profile.settings.nametag.ColorNameType;
import me.dmk.core.profile.settings.nametag.CustomSuffixType;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 02.03.2023
 */

public class NameTagSettingsGui extends PluginGui {
    public NameTagSettingsGui(Player player, Profile profile) {
        super(player, profile, "Zmiana nametagu", 3, true, true);
    }

    @Override
    public void build() {
        ProfileSettings profileSettings = this.profile.getProfileSettings();

        ColorNameType colorNameType = profileSettings.getColorName();
        CustomSuffixType customSuffixType = profileSettings.getCustomSuffix();

        GuiItem colorNameItem = ItemBuilder.from(Material.RED_DYE)
                .name(ComponentUtil.text(StringFormatter.formatPurpleGradient() + "Zmiana koloru nicku"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualny kolor nicku<dark_gray>:",
                        this.circle + " " + colorNameType.getFormat() + this.profile.getName(),
                        "",
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić kolor nicku",
                        ""
                ))
                .asGuiItem(event -> {
                    if (!this.player.hasPermission("core.nametag.colorname")) {
                        new BarrierBuilder()
                                .name("<red>Nie posiadasz dostępu do tej funkcji")
                                .lore(
                                        "",
                                        this.circle + " <gray>Uprawnienie<dark_gray>: <red>core.nametag.colorname",
                                        ""
                                )
                                .updateItem(this.gui, event.getSlot());
                        return;
                    }

                    new ColorNameGui(this.player, this.profile).open();
                });

        GuiItem customSuffixItem = ItemBuilder.from(Material.RED_DYE)
                .name(ComponentUtil.text(StringFormatter.formatPurpleGradient() + "Zmiana suffixu"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualny suffix<dark_gray>:",
                        this.circle + " " + profileSettings.getColorName().getFormat() + profile.getName() + " " + customSuffixType.getFormat(),
                        "",
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić suffix",
                        ""
                ))
                .asGuiItem(event -> {
                    if (!this.player.hasPermission("core.nametag.suffix")) {
                        new BarrierBuilder()
                                .name("<red>Nie posiadasz dostępu do tej funkcji")
                                .lore(
                                        "",
                                        this.circle + " <gray>Uprawnienie<dark_gray>: <red>core.nametag.suffix",
                                        ""
                                )
                                .updateItem(this.gui, event.getSlot());
                        return;
                    }

                    new CustomSuffixGui(this.player, this.profile).open();
                });

        GuiItem backButton = this.createBackButton(event ->
                        new ProfileSettingsGui(this.player, this.profile).open(),
                "",
                this.circle + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do menu ustawień<dark_gray>.",
                ""
        );

        this.gui.setItem(12, colorNameItem);
        this.gui.setItem(14, customSuffixItem);

        this.gui.setItem(22, backButton);
    }
}

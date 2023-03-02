package me.dmk.core.profile.settings.nametag.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.gui.item.builder.BarrierBuilder;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.settings.nametag.ColorNameType;
import me.dmk.core.util.ComponentUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 02.03.2023
 */

public class ColorNameGui extends PluginGui {
    public ColorNameGui(Player player, Profile profile) {
        super(player, profile, "Ustawienia koloru nicku", 6, true, true);
    }

    @Override
    public void build() {
        GuiItem grayColorItem = ItemBuilder.from(Material.GRAY_DYE)
                .name(ComponentUtil.text(ColorNameType.DEAFULT.getFormat() + "Szary"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + ColorNameType.DEAFULT.getFormat() + this.profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(event.getSlot(), ColorNameType.DEAFULT)
                );

        GuiItem whiteColorItem = ItemBuilder.from(Material.WHITE_DYE)
                .name(ComponentUtil.text(ColorNameType.WHITE.getFormat() + "Biały"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + ColorNameType.WHITE.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(event.getSlot(), ColorNameType.WHITE)
                );

        GuiItem purpleColorItem = ItemBuilder.from(Material.PURPLE_DYE)
                .name(ComponentUtil.text(ColorNameType.PURPLE.getFormat() + "Różowy"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + ColorNameType.PURPLE.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(event.getSlot(), ColorNameType.PURPLE)
                );

        GuiItem aquaColorItem = ItemBuilder.from(Material.LIGHT_BLUE_DYE)
                .name(ComponentUtil.text(ColorNameType.AQUA.getFormat() + "Jasny niebieski"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + ColorNameType.AQUA.getFormat() + this.profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(event.getSlot(), ColorNameType.AQUA)
                );

        GuiItem yellowColorItem = ItemBuilder.from(Material.YELLOW_DYE)
                .name(ComponentUtil.text(ColorNameType.YELLOW.getFormat() + "Żółty"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + ColorNameType.YELLOW.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(event.getSlot(), ColorNameType.YELLOW)
                );

        GuiItem goldColorItem = ItemBuilder.from(Material.ORANGE_DYE)
                .name(ComponentUtil.text(ColorNameType.GOLD.getFormat() + "Złoty"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + ColorNameType.GOLD.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(event.getSlot(), ColorNameType.GOLD)
                );

        GuiItem blackColorItem = ItemBuilder.from(Material.GREEN_DYE)
                .name(ComponentUtil.text(ColorNameType.GREEN.getFormat() + "Zielony"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + ColorNameType.GREEN.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(event.getSlot(), ColorNameType.GREEN)
                );

        GuiItem rainbowColorItem = ItemBuilder.from(Material.ORANGE_GLAZED_TERRACOTTA)
                .name(ComponentUtil.text(ColorNameType.RAINBOW.getFormat() + "Kolorowy"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + ColorNameType.RAINBOW.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(event.getSlot(), ColorNameType.RAINBOW)
                );

        GuiItem goldYellowGradientItem = ItemBuilder.from(Material.YELLOW_GLAZED_TERRACOTTA)
                .name(ComponentUtil.text(ColorNameType.GOLD_YELLOW_GRADIENT.getFormat() + "Złoty-żółty gradient"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + ColorNameType.GOLD_YELLOW_GRADIENT.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(event.getSlot(), ColorNameType.GOLD_YELLOW_GRADIENT)
                );

        GuiItem greenGradientItem = ItemBuilder.from(Material.GREEN_GLAZED_TERRACOTTA)
                .name(ComponentUtil.text(ColorNameType.GREEN_GRADIENT.getFormat() + "Zielony-ciemny zielony gradient"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + ColorNameType.GREEN_GRADIENT.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(event.getSlot(), ColorNameType.GREEN_GRADIENT)
                );

        GuiItem purpleGradientItem = ItemBuilder.from(Material.MAGENTA_GLAZED_TERRACOTTA)
                .name(ComponentUtil.text(ColorNameType.PURPLE_GRADIENT.getFormat() + "Różowy-ciemny różowy gradient"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + ColorNameType.PURPLE_GRADIENT.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(event.getSlot(), ColorNameType.PURPLE_GRADIENT)
                );

        GuiItem aquaGradientItem = ItemBuilder.from(Material.LIGHT_BLUE_GLAZED_TERRACOTTA)
                .name(ComponentUtil.text(ColorNameType.AQUA_GRADIENT.getFormat() + "Jasny-niebieski gradient"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + ColorNameType.AQUA_GRADIENT.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(event.getSlot(), ColorNameType.AQUA_GRADIENT)
                );

        GuiItem blueGradientItem = ItemBuilder.from(Material.BLUE_GLAZED_TERRACOTTA)
                .name(ComponentUtil.text(ColorNameType.BLUE_GRADIENT.getFormat() + "Jasny niebieski-ciemny niebieski gradient"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + ColorNameType.BLUE_GRADIENT.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(event.getSlot(), ColorNameType.BLUE_GRADIENT)
                );

        GuiItem grayGradientItem = ItemBuilder.from(Material.GRAY_GLAZED_TERRACOTTA)
                .name(ComponentUtil.text(ColorNameType.GRAY_GRADIENT.getFormat() + "Szary-ciemny szary gradient"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + ColorNameType.GRAY_GRADIENT.getFormat() + this.profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(event.getSlot(), ColorNameType.GRAY_GRADIENT)
                );

        GuiItem backButton = this.createBackButton(event ->
                        new NameTagSettingsGui(this.player, this.profile).open(),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do ustawień nametagu<dark_gray>.",
                ""
        );

        this.gui.setItem(11, grayColorItem);
        this.gui.setItem(20, whiteColorItem);
        this.gui.setItem(21, purpleColorItem);
        this.gui.setItem(22, aquaColorItem);
        this.gui.setItem(23, yellowColorItem);

        this.gui.setItem(24, goldColorItem);
        this.gui.setItem(15, blackColorItem);
        this.gui.setItem(29, rainbowColorItem);
        this.gui.setItem(30, goldYellowGradientItem);
        this.gui.setItem(31, greenGradientItem);

        this.gui.setItem(32, purpleGradientItem);
        this.gui.setItem(33, aquaGradientItem);
        this.gui.setItem(38, blueGradientItem);
        this.gui.setItem(42, grayGradientItem);

        this.gui.setItem(49, backButton);
    }

    private void updateColor(int slot, ColorNameType colorNameType) {
        if (this.profile.getProfileSettings().getColorName().equals(colorNameType))  {
            new BarrierBuilder()
                    .name("<red>Posiadasz aktualnie ustawiony ten kolor")
                    .updateItem(this.gui, slot);
            return;
        }

        this.profile.getProfileSettings().setColorName(colorNameType);
        this.open();
    }
}

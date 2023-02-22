package me.dmk.core.profile.settings.nametag.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.gui.item.builder.BarrierBuilder;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.settings.nametag.ColorNameType;
import me.dmk.core.util.ComponentUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 19.01.2023
 */

public class ColorNameGui extends PluginGui {

    public void open(Player player, Profile profile) {
        ColorNameType gray = ColorNameType.DEAFULT;
        ColorNameType white = ColorNameType.WHITE;
        ColorNameType purple = ColorNameType.PURPLE;
        ColorNameType aqua = ColorNameType.AQUA;
        ColorNameType yellow = ColorNameType.YELLOW;
        ColorNameType gold = ColorNameType.GOLD;
        ColorNameType green = ColorNameType.GREEN;
        ColorNameType rainbow = ColorNameType.RAINBOW;
        ColorNameType goldYellowGradient = ColorNameType.GOLD_YELLOW_GRADIENT;
        ColorNameType greenGradient = ColorNameType.GREEN_GRADIENT;
        ColorNameType purpleGradient = ColorNameType.PURPLE_GRADIENT;
        ColorNameType aquaGradient = ColorNameType.AQUA_GRADIENT;
        ColorNameType blueGradient = ColorNameType.BLUE_GRADIENT;
        ColorNameType grayGradient = ColorNameType.GRAY_GRADIENT;

        Gui gui = Gui.gui()
                .title(ComponentUtil.text(this.circle + " <light_purple>Edytowanie koloru nicku " + this.circle))
                .rows(6)
                .disableAllInteractions()
                .create();

        GuiItem grayColorItem = ItemBuilder.from(Material.GRAY_DYE)
                .name(ComponentUtil.text(gray.getFormat() + "Szary"))
                .lore(ComponentUtil.asList(
                    "",
                    this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                    this.circle + " " + gray.getFormat() + profile.getName(),
                    ""
                ))
                .asGuiItem(event ->
                        this.updateColor(player, profile, gui, event.getSlot(), gray)
                );

        GuiItem whiteColorItem = ItemBuilder.from(Material.WHITE_DYE)
                .name(ComponentUtil.text(white.getFormat() + "Biały"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + white.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(player, profile, gui, event.getSlot(), white)
                );

        GuiItem purpleColorItem = ItemBuilder.from(Material.PURPLE_DYE)
                .name(ComponentUtil.text(purple.getFormat() + "Różowy"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + purple.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(player, profile, gui, event.getSlot(), purple)
                );

        GuiItem aquaColorItem = ItemBuilder.from(Material.LIGHT_BLUE_DYE)
                .name(ComponentUtil.text(aqua.getFormat() + "Jasny niebieski"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + aqua.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(player, profile, gui, event.getSlot(), aqua)
                );

        GuiItem yellowColorItem = ItemBuilder.from(Material.YELLOW_DYE)
                .name(ComponentUtil.text(yellow.getFormat() + "Żółty"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + yellow.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(player, profile, gui, event.getSlot(), yellow)
                );

        GuiItem goldColorItem = ItemBuilder.from(Material.ORANGE_DYE)
                .name(ComponentUtil.text(gold.getFormat() + "Złoty"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + gold.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(player, profile, gui, event.getSlot(), gold)
                );

        GuiItem blackColorItem = ItemBuilder.from(Material.GREEN_DYE)
                .name(ComponentUtil.text(green.getFormat() + "Zielony"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + green.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(player, profile, gui, event.getSlot(), green)
                );

        GuiItem rainbowColorItem = ItemBuilder.from(Material.ORANGE_GLAZED_TERRACOTTA)
                .name(ComponentUtil.text(rainbow.getFormat() + "Kolorowy"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + rainbow.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(player, profile, gui, event.getSlot(), rainbow)
                );

        GuiItem goldYellowGradientItem = ItemBuilder.from(Material.YELLOW_GLAZED_TERRACOTTA)
                .name(ComponentUtil.text(goldYellowGradient.getFormat() + "Złoty-żółty gradient"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + goldYellowGradient.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(player, profile, gui, event.getSlot(), goldYellowGradient)
                );

        GuiItem greenGradientItem = ItemBuilder.from(Material.GREEN_GLAZED_TERRACOTTA)
                .name(ComponentUtil.text(greenGradient.getFormat() + "Zielony-ciemny zielony gradient"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + greenGradient.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateColor(player, profile, gui, event.getSlot(), greenGradient)
                );

        GuiItem purpleGradientItem = ItemBuilder.from(Material.MAGENTA_GLAZED_TERRACOTTA)
                .name(ComponentUtil.text(purpleGradient.getFormat() + "Różowy-ciemny różowy gradient"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + purpleGradient.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event -> this.updateColor(player, profile, gui, event.getSlot(), purpleGradient));

        GuiItem aquaGradientItem = ItemBuilder.from(Material.LIGHT_BLUE_GLAZED_TERRACOTTA)
                .name(ComponentUtil.text(aquaGradient.getFormat() + "Jasny-niebieski gradient"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + aquaGradient.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event -> this.updateColor(player, profile, gui, event.getSlot(), aquaGradient));

        GuiItem blueGradientItem = ItemBuilder.from(Material.BLUE_GLAZED_TERRACOTTA)
                .name(ComponentUtil.text(blueGradient.getFormat() + "Jasny niebieski-ciemny niebieski gradient"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + blueGradient.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event -> this.updateColor(player, profile, gui, event.getSlot(), blueGradient));

        GuiItem grayGradientItem = ItemBuilder.from(Material.GRAY_GLAZED_TERRACOTTA)
                .name(ComponentUtil.text(grayGradient.getFormat() + "Szary-ciemny szary gradient"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + grayGradient.getFormat() + profile.getName(),
                        ""
                ))
                .asGuiItem(event -> this.updateColor(player, profile, gui, event.getSlot(), grayGradient));

        GuiItem backButton = this.createBackButton(event ->
                        new NameTagSettingsGui().open(player, profile),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do ustawień nametagu<dark_gray>.",
                ""
        );

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());

        gui.setItem(11, grayColorItem);
        gui.setItem(20, whiteColorItem);
        gui.setItem(21, purpleColorItem);
        gui.setItem(22, aquaColorItem);
        gui.setItem(23, yellowColorItem);

        gui.setItem(24, goldColorItem);
        gui.setItem(15, blackColorItem);
        gui.setItem(29, rainbowColorItem);
        gui.setItem(30, goldYellowGradientItem);
        gui.setItem(31, greenGradientItem);

        gui.setItem(32, purpleGradientItem);
        gui.setItem(33, aquaGradientItem);
        gui.setItem(38, blueGradientItem);
        gui.setItem(42, grayGradientItem);

        gui.setItem(49, backButton);

        gui.open(player);
    }

    private void updateColor(Player player, Profile profile, Gui gui, int slot, ColorNameType colorNameType) {
        if (profile.getProfileSettings().getColorName().equals(colorNameType)) {
            new BarrierBuilder()
                    .name("<red>Posiadasz aktualnie ustawiony ten kolor")
                    .updateGui(gui, slot);
            return;
        }

        profile.getProfileSettings().setColorName(colorNameType);
        this.open(player, profile);
    }
}

package me.dmk.core.profile.settings.nametag.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.gui.item.builder.BarrierBuilder;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.settings.nametag.CustomSuffixType;
import me.dmk.core.util.ComponentUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 02.03.2023
 */

public class CustomSuffixGui extends PluginGui {

    private final Profile profile;

    public CustomSuffixGui(Player player, Profile profile) {
        super(player, "Zmiana suffixu", 5, true, true);

        this.profile = profile;
    }

    @Override
    public void build() {
        GuiItem noneItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text("<gray>Brak"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + this.profile.getColoredName(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateSuffix(event.getSlot(), CustomSuffixType.NONE)
                );

        GuiItem heartItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(CustomSuffixType.HEART.getFormat()))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + this.profile.getColoredName() + " " + CustomSuffixType.HEART.getFormat(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateSuffix(event.getSlot(), CustomSuffixType.HEART)
                );

        GuiItem flowerItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(CustomSuffixType.FLOWER.getFormat()))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + this.profile.getColoredName() + " " + CustomSuffixType.FLOWER.getFormat(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateSuffix(event.getSlot(), CustomSuffixType.FLOWER)
                );

        GuiItem smileItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(CustomSuffixType.SMILE.getFormat()))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + this.profile.getColoredName() + " " + CustomSuffixType.SMILE.getFormat(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateSuffix(event.getSlot(), CustomSuffixType.SMILE)
                );

        GuiItem unbrellaItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(CustomSuffixType.UNBRELLA.getFormat()))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + this.profile.getColoredName() + " " + CustomSuffixType.UNBRELLA.getFormat(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateSuffix(event.getSlot(), CustomSuffixType.UNBRELLA)
                );

        GuiItem crucifixItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(CustomSuffixType.CRUCIFIX.getFormat()))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + this.profile.getColoredName() + " " + CustomSuffixType.CRUCIFIX.getFormat(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateSuffix(event.getSlot(), CustomSuffixType.CRUCIFIX)
                );

        GuiItem deathItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(CustomSuffixType.DEATH.getFormat()))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + this.profile.getColoredName() + " " + CustomSuffixType.DEATH.getFormat(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateSuffix(event.getSlot(), CustomSuffixType.DEATH)
                );

        GuiItem starItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(CustomSuffixType.STAR.getFormat()))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + this.profile.getColoredName() + " " + CustomSuffixType.STAR.getFormat(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateSuffix(event.getSlot(), CustomSuffixType.STAR)
                );

        GuiItem cloudItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(CustomSuffixType.CLOUD.getFormat()))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + this.profile.getColoredName() + " " + CustomSuffixType.CLOUD.getFormat(),
                        ""
                ))
                .asGuiItem(event ->
                        this.updateSuffix(event.getSlot(), CustomSuffixType.CLOUD)
                );

        GuiItem backButton = this.createBackButton(event ->
                        new NameTagSettingsGui(this.player, this.profile).open(),
                "",
                this.warning + " <gold>Kliknij<dark_gray>, <gray>aby powrócić do ustawień nametagu<dark_gray>.",
                ""
        );

        gui.setItem(12, noneItem);
        gui.setItem(13, heartItem);
        gui.setItem(14, flowerItem);

        gui.setItem(21, smileItem);
        gui.setItem(22, unbrellaItem);
        gui.setItem(23, crucifixItem);

        gui.setItem(30, deathItem);
        gui.setItem(31, starItem);
        gui.setItem(32, cloudItem);

        gui.setItem(40, backButton);
    }

    private void updateSuffix(int slot, CustomSuffixType customSuffixType) {
        if (this.profile.getProfileSettings().getCustomSuffix().equals(customSuffixType)) {
            new BarrierBuilder()
                    .name("<red>Posiadasz aktualnie ustawiony ten suffix")
                    .updateItem(this.gui, slot);
            return;
        }

        this.profile.getProfileSettings().setCustomSuffix(customSuffixType);
        this.open();
    }
}

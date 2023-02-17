package me.dmk.core.profile.settings.nametag.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.AllArgsConstructor;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.gui.item.builder.BarrierBuilder;
import me.dmk.core.gui.item.storage.ItemStorage;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.profile.settings.nametag.CustomSuffixType;
import me.dmk.core.util.ComponentUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 19.01.2023
 */

@AllArgsConstructor
public class CustomSuffixGui extends ItemStorage {

    private final PluginConfiguration pluginConfiguration;
    private final LuckPermsController luckPermsController;
    private final ProfileController profileController;
    private final ProfileCache profileCache;
    private final GuildCache guildCache;

    public void open(Player player, Profile profile) {
        ProfileSettings profileSettings = profile.getProfileSettings();
        String colorNameFormat = profileSettings.getColorName().getFormat();

        CustomSuffixType heart = CustomSuffixType.HEART;
        CustomSuffixType flower = CustomSuffixType.FLOWER;
        CustomSuffixType smile = CustomSuffixType.SMILE;
        CustomSuffixType unbrella = CustomSuffixType.UNBRELLA;
        CustomSuffixType crucifix = CustomSuffixType.CRUCIFIX;
        CustomSuffixType death = CustomSuffixType.DEATH;
        CustomSuffixType star = CustomSuffixType.STAR;
        CustomSuffixType cloud = CustomSuffixType.CLOUD;

        Gui gui = Gui.gui()
                .title(ComponentUtil.text(this.circle + " <light_purple>Zmiana suffixu " + this.circle))
                .rows(5)
                .disableAllInteractions()
                .create();

        GuiItem noneItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text("<gray>Brak"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + colorNameFormat + profile.getName(),
                        ""
                ))
                .asGuiItem(event -> this.updateSuffix(player, profile, gui, event.getSlot(), CustomSuffixType.NONE));

        GuiItem heartItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(heart.getFormat()))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + colorNameFormat + profile.getName() + " " + heart.getFormat(),
                        ""
                ))
                .asGuiItem(event -> this.updateSuffix(player, profile, gui, event.getSlot(), heart));

        GuiItem flowerItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(flower.getFormat()))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + colorNameFormat + profile.getName() + " " + flower.getFormat(),
                        ""
                ))
                .asGuiItem(event -> this.updateSuffix(player, profile, gui, event.getSlot(), flower));

        GuiItem smileItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(smile.getFormat()))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + colorNameFormat + profile.getName() + " " + smile.getFormat(),
                        ""
                ))
                .asGuiItem(event -> this.updateSuffix(player, profile, gui, event.getSlot(), smile));

        GuiItem unbrellaItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(unbrella.getFormat()))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + colorNameFormat + profile.getName() + " " + unbrella.getFormat(),
                        ""
                ))
                .asGuiItem(event -> this.updateSuffix(player, profile, gui, event.getSlot(), unbrella));

        GuiItem crucifixItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(crucifix.getFormat()))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + colorNameFormat + profile.getName() + " " + crucifix.getFormat(),
                        ""
                ))
                .asGuiItem(event -> this.updateSuffix(player, profile, gui, event.getSlot(), crucifix));

        GuiItem deathItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(death.getFormat()))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + colorNameFormat + profile.getName() + " " + death.getFormat(),
                        ""
                ))
                .asGuiItem(event -> this.updateSuffix(player, profile, gui, event.getSlot(), death));

        GuiItem starItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(star.getFormat()))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + colorNameFormat + profile.getName() + " " + star.getFormat(),
                        ""
                ))
                .asGuiItem(event -> this.updateSuffix(player, profile, gui, event.getSlot(), star));

        GuiItem cloudItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(cloud.getFormat()))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Twój nick będzie wyglądał<dark_gray>:",
                        this.circle + " " + colorNameFormat + profile.getName() + " " + cloud.getFormat(),
                        ""
                ))
                .asGuiItem(event -> this.updateSuffix(player, profile, gui, event.getSlot(), cloud));

        GuiItem backButton = this.createBackButton(event ->
                        new NameTagSettingsGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do ustawień nametagu<dark_gray>.",
                ""
        );

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());

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

        gui.open(player);
    }

    private void updateSuffix(Player player, Profile profile, Gui gui, int slot, CustomSuffixType customSuffixType) {
        if (profile.getProfileSettings().getCustomSuffix().equals(customSuffixType)) {
            new BarrierBuilder()
                    .name("<red>Posiadasz aktualnie ustawiony ten suffix")
                    .updateGui(gui, slot);
            return;
        }

        profile.getProfileSettings().setCustomSuffix(customSuffixType);
        this.open(player, profile);
    }
}

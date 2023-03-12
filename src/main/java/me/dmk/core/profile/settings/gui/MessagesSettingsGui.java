package me.dmk.core.profile.settings.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 02.03.2023
 */

public class MessagesSettingsGui extends PluginGui {

    private final Profile profile;

    public MessagesSettingsGui(Player player, Profile profile) {
        super(player, "Ustawienia wiadomości", 6, true, true);

        this.profile = profile;
    }

    @Override
    public void build() {
        ProfileSettings profileSettings = this.profile.getProfileSettings();

        GuiItem privateMessagesItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(StringFormatter.formatPurpleGradient() + "Prywatne wiadomości"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualny status<dark_gray>: " + StringFormatter.formatBoolean(profileSettings.isPrivateMessages()),
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić status<dark_gray>.",
                        ""
                ))
                .glow(profileSettings.isPrivateMessages())
                .asGuiItem(event -> {
                    profileSettings.setPrivateMessages(!profileSettings.isPrivateMessages());
                    this.open();
                });

        GuiItem achievementsItem = ItemBuilder.from(Material.BOOK)
                .name(ComponentUtil.text(StringFormatter.formatPurpleGradient() + "Wiadomości osiągnięć"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualny status<dark_gray>: " + StringFormatter.formatBoolean(profileSettings.isAchievementsMessages()),
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić status<dark_gray>.",
                        ""
                ))
                .glow(profileSettings.isAchievementsMessages())
                .asGuiItem(event -> {
                    profileSettings.setAchievementsMessages(!profileSettings.isAchievementsMessages());
                    this.open();
                });

        GuiItem deathMessagesItem = ItemBuilder.from(Material.DIAMOND_SWORD)
                .name(ComponentUtil.text(StringFormatter.formatPurpleGradient() + "Wiadomości o zabójstwach"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualny status<dark_gray>: " + StringFormatter.formatBoolean(profileSettings.isDeathMessages()),
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić status<dark_gray>.",
                        ""
                ))
                .glow(profileSettings.isDeathMessages())
                .asGuiItem(event -> {
                    profileSettings.setDeathMessages(!profileSettings.isDeathMessages());
                    this.open();
                });

        GuiItem systemMessagesItem = ItemBuilder.from(Material.COMMAND_BLOCK)
                .name(ComponentUtil.text(StringFormatter.formatPurpleGradient() + "Automatyczne wiadomości"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualny status<dark_gray>: " + StringFormatter.formatBoolean(profileSettings.isSystemMessages()),
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić status<dark_gray>.",
                        ""
                ))
                .glow(profileSettings.isSystemMessages())
                .asGuiItem(event -> {
                    profileSettings.setSystemMessages(!profileSettings.isSystemMessages());
                    this.open();
                });

        GuiItem guildMessagesItem = ItemBuilder.from(Material.BEACON)
                .name(ComponentUtil.text(StringFormatter.formatPurpleGradient() + "Wiadomości o gildiach"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualny status<dark_gray>: " + StringFormatter.formatBoolean(profileSettings.isGuildMessages()),
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić status<dark_gray>.",
                        ""
                ))
                .glow(profileSettings.isGuildMessages())
                .asGuiItem(event -> {
                    profileSettings.setGuildMessages(!profileSettings.isGuildMessages());
                    this.open();
                });

        GuiItem globalMessagesItem = ItemBuilder.from(Material.PLAYER_HEAD)
                .name(ComponentUtil.text(StringFormatter.formatPurpleGradient() + "Globalne wiadomości graczy"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualny status<dark_gray>: " + StringFormatter.formatBoolean(profileSettings.isGlobalMessages()),
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić status<dark_gray>.",
                        ""
                ))
                .glow(profileSettings.isGlobalMessages())
                .asGuiItem(event -> {
                    profileSettings.setGlobalMessages(!profileSettings.isGlobalMessages());
                    this.open();
                });

        GuiItem backButton = this.createBackButton(event ->
                        new ProfileSettingsGui(this.player, this.profile).open(),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do ustawień profilu<dark_gray>.",
                ""
        );

        this.gui.setItem(21, privateMessagesItem);
        this.gui.setItem(22, achievementsItem);
        this.gui.setItem(23, deathMessagesItem);

        this.gui.setItem(30, systemMessagesItem);
        this.gui.setItem(31, guildMessagesItem);
        this.gui.setItem(32, globalMessagesItem);

        this.gui.setItem(49, backButton);
    }
}

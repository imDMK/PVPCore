package me.dmk.core.profile.settings.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.AllArgsConstructor;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.gui.item.storage.ItemStorage;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 19.01.2023
 */

@AllArgsConstructor
public class MessagesSettingsGui extends ItemStorage {

    private final PluginConfiguration pluginConfiguration;
    private final LuckPermsController luckPermsController;
    private final ProfileController profileController;
    private final ProfileCache profileCache;
    private final GuildCache guildCache;

    public void open(Player player, Profile profile) {
        Gui gui = Gui.gui()
                .title(ComponentUtil.text(this.circle + " <light_purple>Ustawienia wiadomości " + this.circle))
                .rows(6)
                .disableAllInteractions()
                .create();

        ProfileSettings profileSettings = profile.getProfileSettings();

        GuiItem privateMessagesItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Prywatne wiadomości"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualny status<dark_gray>: " + StringFormatter.formatBoolean(profileSettings.isPrivateMessages()),
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić status<dark_gray>.",
                        ""
                ))
                .glow(profileSettings.isPrivateMessages())
                .asGuiItem(event -> {
                    profileSettings.setPrivateMessages(!profileSettings.isPrivateMessages());
                    this.open(player, profile);
                });

        GuiItem achievementsItem = ItemBuilder.from(Material.BOOK)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Wiadomości osiągnięć"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualny status<dark_gray>: " + StringFormatter.formatBoolean(profileSettings.isAchievementsMessages()),
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić status<dark_gray>.",
                        ""
                ))
                .glow(profileSettings.isAchievementsMessages())
                .asGuiItem(event -> {
                    profileSettings.setAchievementsMessages(!profileSettings.isAchievementsMessages());
                    this.open(player, profile);
                });

        GuiItem deathMessagesItem = ItemBuilder.from(Material.DIAMOND_SWORD)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Wiadomości o zabójstwach"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualny status<dark_gray>: " + StringFormatter.formatBoolean(profileSettings.isDeathMessages()),
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić status<dark_gray>.",
                        ""
                ))
                .glow(profileSettings.isDeathMessages())
                .asGuiItem(event -> {
                    profileSettings.setDeathMessages(!profileSettings.isDeathMessages());
                    this.open(player, profile);
                });

        GuiItem systemMessagesItem = ItemBuilder.from(Material.COMMAND_BLOCK)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Automatyczne wiadomości"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualny status<dark_gray>: " + StringFormatter.formatBoolean(profileSettings.isSystemMessages()),
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić status<dark_gray>.",
                        ""
                ))
                .glow(profileSettings.isSystemMessages())
                .asGuiItem(event -> {
                    profileSettings.setSystemMessages(!profileSettings.isSystemMessages());
                    this.open(player, profile);
                });

        GuiItem guildMessagesItem = ItemBuilder.from(Material.BEACON)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Wiadomości o gildiach"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualny status<dark_gray>: " + StringFormatter.formatBoolean(profileSettings.isGuildMessages()),
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić status<dark_gray>.",
                        ""
                ))
                .glow(profileSettings.isGuildMessages())
                .asGuiItem(event -> {
                    profileSettings.setGuildMessages(!profileSettings.isGuildMessages());
                    this.open(player, profile);
                });

        GuiItem globalMessagesItem = ItemBuilder.from(Material.PLAYER_HEAD)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Globalne wiadomości graczy"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualny status<dark_gray>: " + StringFormatter.formatBoolean(profileSettings.isGlobalMessages()),
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby zmienić status<dark_gray>.",
                        ""
                ))
                .glow(profileSettings.isGlobalMessages())
                .asGuiItem(event -> {
                    profileSettings.setGlobalMessages(!profileSettings.isGlobalMessages());
                    this.open(player, profile);
                });

        GuiItem backButton = this.createBackButton(event ->
                        new ProfileSettingsGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do ustawień profilu<dark_gray>.",
                ""
        );

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());

        gui.setItem(21, privateMessagesItem);
        gui.setItem(22, achievementsItem);
        gui.setItem(23, deathMessagesItem);

        gui.setItem(30, systemMessagesItem);
        gui.setItem(31, guildMessagesItem);
        gui.setItem(32, globalMessagesItem);

        gui.setItem(49, backButton);

        gui.open(player);
    }
}

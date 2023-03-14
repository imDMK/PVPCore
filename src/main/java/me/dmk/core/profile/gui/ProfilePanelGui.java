package me.dmk.core.profile.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.CorePlugin;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.gui.item.builder.BarrierBuilder;
import me.dmk.core.gui.item.storage.SkullStorage;
import me.dmk.core.guild.gui.GuildPanelGui;
import me.dmk.core.guild.rank.GuildRank;
import me.dmk.core.kit.Kit;
import me.dmk.core.kit.KitMap;
import me.dmk.core.kit.gui.KitPrewiewGui;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.punishment.PunishmentHistoryGui;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.profile.settings.gui.ProfileSettingsGui;
import me.dmk.core.profile.statistics.ProfileStatistics;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.PlayerUtil;
import me.dmk.core.util.TimeUtil;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.SymbolUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Optional;

/**
 * Created by DMK on 02.03.2023
 */

public class ProfilePanelGui extends PluginGui {

    private final PluginConfiguration pluginConfiguration = CorePlugin.getCorePlugin().getPluginConfiguration();
    private final LuckPermsController luckPermsController = CorePlugin.getCorePlugin().getLuckPermsController();
    private final ProfileController profileController = CorePlugin.getCorePlugin().getProfileController();
    private final KitMap kitMap = CorePlugin.getCorePlugin().getKitMap();

    private final Profile profile;

    public ProfilePanelGui(Player player, Profile profile) {
        super(player, "Panel gracza " + profile.getName(), 6, true, true);

        this.profile = profile;
    }

    @Override
    public void build() {
        boolean isSelf = this.player.getUniqueId().equals(this.profile.getUuid());

        ProfileSettings settings = this.profile.getProfileSettings();
        ProfileStatistics statistics = this.profile.getProfileStatistics();

        String group = this.luckPermsController.getOrElseLoad(this.profile.getUuid())
                .flatMap(u -> this.luckPermsController.getHighestGroupDisplayNameOrName(u.getUniqueId()))
                .orElse("Brak");

        String timeSpent = TimeUtil.durationToString(
                Duration.ofSeconds(isSelf ? PlayerUtil.getSecondsPlayed(this.player) : statistics.getTimeSpent())
        );

        GuiItem playerHead = SkullStorage.createPlayerHead(this.profile.getUuid())
                .name(ComponentUtil.text(this.profile.getColoredName() + " " + settings.getCustomSuffix().getFormat()))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Online<dark_gray>: " + (this.profile.isOnline() ? "<green>Tak" : "<red>Nie <dark_gray>(<gray>Ostatnio widziany<dark_gray>: <red>" + TimeUtil.toDays(this.profile.getLastJoin().toInstant(), false) + " dni temu<dark_gray>)"),
                        this.circle + " <gray>Grupa<dark_gray>: <light_purple>" + group,
                        this.circle + " <gray>Założenie konta<dark_gray>: <light_purple>" + TimeUtil.formatDate(this.profile.getFirstJoin()),
                        this.circle + " <gray>Karany<dark_gray>: " + (this.profile.wasPunished() ? "<red>Tak" : "<green>Nie"),
                        ""
                ))
                .asGuiItem();

        GuiItem statisticsItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(StringFormatter.formatPurpleGradient() + "Statystyki"))
                .lore(ComponentUtil.asList(
                        "",
                        SymbolUtil.getStar("<yellow>") + " <gray>Poziom doświadczenia<dark_gray>: <yellow>" + statistics.getLevel(),
                        SymbolUtil.getStarSecond("<yellow>") + " <gray>Monety<dark_gray>: <yellow>" + statistics.getCoins(),
                        SymbolUtil.getWatch("<gold>") + " <gray>Spędzony czas<dark_gray>: <gold>" + timeSpent,
                        "",
                        SymbolUtil.getSword("<yellow>") + " <gray>Poziom zestawu<dark_gray>: <yellow>" + statistics.getKitLevel(),
                        SymbolUtil.getStar("<gold>") + " <gray>Punkty<dark_gray>: <gold>" + statistics.getPoints(),
                        SymbolUtil.getSword("<red>") + " <gray>Zabójstwa<dark_gray>: <red>" + statistics.getKills(),
                        SymbolUtil.getSword("<red>") + " <gray>Aktualna seria zabójstw<dark_gray>: <red>" + statistics.getKillStreak(),
                        SymbolUtil.getSword("<red>") + " <gray>Najwyższa seria zabójstw<dark_gray>: <red>" + statistics.getHighestKillStreak(),
                        SymbolUtil.getDeath("<gray>") + " <gray>Śmierci<dark_gray>: <gray>" + statistics.getDeaths(),
                        "" + (isSelf ? "<!italic>" + this.warning + " <light_purple>Kilknij<dark_gray>, <gray>aby <light_purple>zresetować <gray>swoje statystyki<dark_gray>." : "")
                ))
                .asGuiItem(event -> {
                    if (!isSelf) {
                        return;
                    }

                    int coinsToResetStatistics = this.pluginConfiguration.getCoinsToResetStatistics();

                    if (coinsToResetStatistics > statistics.getCoins()) {
                        new BarrierBuilder()
                                .name("<red>Nie spełniasz wymagań")
                                .lore("<red>Aby zresetować statystyki potrzebujesz <gold>" + coinsToResetStatistics + " <red>monet<dark_gray>.")
                                .updateItem(this.gui, event.getSlot());
                        return;
                    }

                    Bukkit.dispatchCommand(this.player, "resetstatistics");
                });

        GuiItem kitItem = ItemBuilder.from(Material.SHIELD)
                .name(ComponentUtil.text(StringFormatter.formatPurpleGradient() + "Zestaw"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do poglądu zestawu<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    Optional<Kit> kit = this.kitMap.get(statistics.getKitLevel());
                    if (kit.isEmpty()) {
                        new BarrierBuilder()
                                .name("<red>Nie znaleziono zestawu")
                                .updateItem(this.gui, event.getSlot());
                        return;
                    }

                    KitPrewiewGui kitPrewiewGui = new KitPrewiewGui(this.player, this.profile, kit.get());
                    kitPrewiewGui.build();

                    GuiItem backButton = this.createBackButton(e ->
                                    this.open(),
                            "",
                            this.circle + " <light_purple>Kliknij<dark_gray>, <gray>aby wrócić do panelu profilu<dark_gray>.",
                            ""
                    );

                    kitPrewiewGui.gui.setItem(49, backButton); //Changing the back button in gui
                    kitPrewiewGui.open(false);
                });

        GuiItem friendListItem = ItemBuilder.from(Material.DRAGON_HEAD)
                .name(ComponentUtil.text(StringFormatter.formatPurpleGradient() + "Przyjaciele"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do listy przyjaciół<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    if (this.profile.getFriends().isEmpty()) {
                        new BarrierBuilder()
                                .name("<red>Lista przyjaciół jest pusta...")
                                .updateItem(this.gui, event.getSlot());
                        return;
                    }

                    new ProfileFriendListGui(this.player, this.profile).open();
                });

        GuiItem punishmentsItem = ItemBuilder.from(Material.TARGET)
                .name(ComponentUtil.text(StringFormatter.formatPurpleGradient() + "Historia kar"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do historii kar<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    if (this.profile.getPunishments().isEmpty()) {
                        new BarrierBuilder()
                                .name("<green>Gracz nie posiada historii kar")
                                .lore(
                                        "",
                                        this.circle + " <gray>Zajrzyj tutaj innym razem " + SymbolUtil.getSmile("<green>"),
                                        ""
                                )
                                .updateItem(this.gui, event.getSlot());
                        return;
                    }

                    new PunishmentHistoryGui(this.player, this.profile, null).open();
                });

        GuiItem settingsOrIgnoreItem;
        if (isSelf) {
            settingsOrIgnoreItem = ItemBuilder.from(Material.REPEATER)
                    .name(ComponentUtil.text(StringFormatter.formatPurpleGradient() + "Ustawienia"))
                    .lore(ComponentUtil.asList(
                            "",
                            this.circle + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do ustawień profilu<dark_gray>.",
                            ""
                    ))
                    .asGuiItem(event ->
                            new ProfileSettingsGui(this.player, this.profile).open()
                    );
        } else {
            boolean playerIgnoredProfile = this.profileController.get(this.player.getUniqueId())
                    .map(Profile::getProfileSettings)
                    .map(profileSettings -> profileSettings.getIgnoredPlayers().contains(this.profile.getUuid()))
                    .orElse(false);

            settingsOrIgnoreItem = ItemBuilder.from(playerIgnoredProfile ? Material.LIME_DYE : Material.RED_DYE)
                    .name(ComponentUtil.text(StringFormatter.formatPurpleGradient() + (playerIgnoredProfile ? "Odblokuj" : "Zablokuj") + " " + this.profile.getName()))
                    .lore(ComponentUtil.asList(
                            "",
                            this.circle + " <light_purple>Kliknij<dark_gray>, <gray>aby " + (playerIgnoredProfile ? StringFormatter.formatGreenGradient() + "odblokować" : StringFormatter.formatRedGradient() + "zablokować") + " <gray>gracza<dark_gray>.",
                            ""
                    ))
                    .asGuiItem(event -> {
                        Bukkit.dispatchCommand(this.player, "ignore " + this.profile.getName());
                        this.open();
                    });
        }

        this.profile.getGuild().ifPresent(guild -> {
            GuildRank guildRank = guild.getGuildRank(this.profile.getUuid());

            GuiItem guildItem = ItemBuilder.from(Material.BEACON)
                    .name(ComponentUtil.text(me.dmk.core.util.string.StringFormatter.formatPurpleGradient() + guild.getTag()))
                    .lore(ComponentUtil.asList(
                            "",
                            this.circle + " <gray>Gracz <light_purple>" + this.profile.getName() + " <gray>posiada rangę <light_purple>" + guildRank.getName() + " <gray>w gildii <light_purple>" + guild.getTag() + "<dark_gray>.",
                            this.circle + " <light_purple>Kilknij LPM<dark_gray>, <gray>aby przejść do panelu tej gildii<dark_gray>.",
                            ""
                    ))
                    .asGuiItem(event ->
                            new GuildPanelGui(this.player, this.profile, guild).open()
                    );

            this.gui.setItem(40, guildItem);
        });

        this.gui.setItem(13, playerHead);
        this.gui.setItem(21, statisticsItem);
        this.gui.setItem(22, kitItem);
        this.gui.setItem(23, friendListItem);
        this.gui.setItem(30, punishmentsItem);
        this.gui.setItem(31, settingsOrIgnoreItem);
    }
}

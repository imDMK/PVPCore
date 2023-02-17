package me.dmk.core.profile.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.AllArgsConstructor;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.gui.item.builder.BarrierBuilder;
import me.dmk.core.gui.item.storage.ItemStorage;
import me.dmk.core.gui.item.storage.SkullStorage;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.guild.gui.GuildPanelGui;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.punishment.PunishmentHistoryGui;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.profile.settings.gui.ProfileSettingsGui;
import me.dmk.core.profile.statistics.ProfileStatistics;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.PlayerUtil;
import me.dmk.core.util.TimeUtil;
import me.dmk.core.util.string.StringUtil;
import me.dmk.core.util.string.SymbolUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Optional;

/**
 * Created by DMK on 17.01.2023
 */

@AllArgsConstructor
public class ProfilePanelGui extends ItemStorage {

    private final PluginConfiguration pluginConfiguration;
    private final LuckPermsController luckPermsController;
    private final ProfileController profileController;
    private final ProfileCache profileCache;
    private final GuildCache guildCache;

    public void open(Player player, Profile profile) {
        Gui gui = Gui.gui()
                .title(ComponentUtil.text(this.circle + " <light_purple>Panel gracza " + this.circle))
                .rows(6)
                .disableAllInteractions()
                .create();
        
        boolean isSelf = player.getUniqueId().equals(profile.getUuid());

        ProfileSettings settings = profile.getProfileSettings();
        ProfileStatistics statistics = profile.getProfileStatistics();

        Optional<String> group = this.luckPermsController.getOrElseLoad(profile.getUuid())
                .flatMap(u -> this.luckPermsController.getHighestGroupDisplayNameOrName(profile.getUuid()));
        
        long lastSeenDays = TimeUtil.toDays(profile.getLastJoin().toInstant(), false);
        int timeSpent = (isSelf ? PlayerUtil.getSecondsPlayed(player) : statistics.getTimeSpent());

        String lastSeen = lastSeenDays == 0L ? "dzisiaj o " + TimeUtil.formatTime(profile.getLastJoin().toInstant()) : lastSeenDays + " dni temu";
        String timePlayed = TimeUtil.durationToString(Duration.ofSeconds(timeSpent));

        GuiItem headItem = SkullStorage
                .createPlayerHead(profile.getUuid())
                .name(ComponentUtil.text(settings.getColorName().getFormat() + profile.getName() + " " + settings.getCustomSuffix().getFormat()))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Online<dark_gray>: " + (profile.getPlayer().isPresent() ? "<green>Tak" : "<red>Nie <dark_gray>- <gray>Ostatnio widziany<dark_gray>: <light_purple>" + lastSeen + "<dark_gray>."),
                        "",
                        this.circle + " <gray>Grupa<dark_gray>: <light_purple>" + group.orElse("Brak"),
                        this.circle + " <gray>Założenie konta<dark_gray>: <light_purple>" + TimeUtil.format(profile.getFirstJoin().toInstant()),
                        this.circle + " <gray>Karany<dark_gray>: " + (profile.getPunishments().isEmpty() ? "<green>Nie" : "<red>Tak"),
                        this.circle + " <gray>Odwiedził nas <light_purple>" + statistics.getEntrances() + " razy <green>" + SymbolUtil.getSmile(),
                        ""
                ))
                .asGuiItem();

        GuiItem statisticsItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Statystyki"))
                .lore(ComponentUtil.asList(
                        "",
                        "<yellow>" + SymbolUtil.getStar() + " <gray>Poziom doświadczenia<dark_gray>: <yellow>" + statistics.getLevel(),
                        "<yellow>" + SymbolUtil.getStarSecond() + " <gray>Monety<dark_gray>: <yellow>" + statistics.getCoins(),
                        "<gold>" + SymbolUtil.getWatch() + " <gray>Spędzony czas<dark_gray>: <gold>" + timePlayed,
                        "",
                        "<gold>" + SymbolUtil.getStar() + " <gray>Punkty<dark_gray>: <gold>" + statistics.getPoints(),
                        "<red>" + SymbolUtil.getSword() + " <gray>Zabójstwa<dark_gray>: <red>" + statistics.getKills(),
                        "<red>" + SymbolUtil.getSword() + " <gray>Aktualna seria zabójstw<dark_gray>: <red>" + statistics.getKillStreak(),
                        "<red>" + SymbolUtil.getSword() + " <gray>Najwyższa seria zabójstw<dark_gray>: <red>" + statistics.getHighestKillStreak(),
                        "<gray>" + SymbolUtil.getDeath() + " <gray>Śmierci<dark_gray>: <gray>" + statistics.getDeaths(),
                        "" + (isSelf ? "<!italic>" + this.warning + " <light_purple>Kilknij<dark_gray>, <gray>aby <light_purple>zresetować <gray>swoje statystyki<dark_gray>." : "")
                ))
                .asGuiItem(event -> {
                    if (!isSelf) {
                        return;
                    }

                    int coinsToResetStatistics = this.pluginConfiguration.getCoinsToResetStatistics();

                    if (statistics.getCoins() > coinsToResetStatistics) {
                        new BarrierBuilder()
                                .name("<red>Nie spełniasz wymagań")
                                .lore(this.warning + " <red>Aby zresetować statystyki potrzebujesz <gold>" + coinsToResetStatistics + " <red>monet<dark_gray>.")
                                .updateGui(gui, event.getSlot());
                        return;
                    }

                    Bukkit.dispatchCommand(player, "resetstatistics");
                });

        GuiItem kitItem = ItemBuilder.from(Material.SHIELD)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Zestaw"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do poglądu zestawu<dark_gray>.",
                        ""
                ))
                .asGuiItem();

        GuiItem punishmentsItem = ItemBuilder.from(Material.TARGET)
                        .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Historia kar"))
                        .lore(ComponentUtil.asList(
                                "",
                                this.circle + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do historii kar<dark_gray>.",
                                ""
                        ))
                        .asGuiItem(event -> {
                            if (profile.getPunishments().isEmpty()) {
                                new BarrierBuilder()
                                        .name("<green>Gracz nie posiada historii kar")
                                        .lore(
                                                "",
                                                this.circle + " <gray>Zajrzyj tutaj innym razem <green>" + SymbolUtil.getSmile(),
                                                ""
                                        )
                                        .updateGui(gui, event.getSlot());
                                return;
                            }

                            new PunishmentHistoryGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache)
                                    .open(player, profile);
                        });

        GuiItem settingsOrIgnoreItem;
        if (isSelf) {
            settingsOrIgnoreItem = ItemBuilder.from(Material.REPEATER)
                    .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Ustawienia"))
                    .lore(ComponentUtil.asList(
                            "",
                            this.circle + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do ustawień profilu<dark_gray>.",
                            ""
                    ))
                    .asGuiItem(event ->
                            new ProfileSettingsGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile)
                    );
        } else {
            Optional<Profile> playerProfile = this.profileCache.get(player.getUniqueId());

            boolean playerIgnoredProfile = playerProfile.map(p -> p.getProfileSettings().getIgnoredPlayers().contains(profile.getUuid()))
                    .orElse(false);

            settingsOrIgnoreItem = ItemBuilder.from(playerIgnoredProfile ? Material.LIME_DYE : Material.RED_DYE)
                    .name(ComponentUtil.text(StringUtil.getPurpleGradient() + (playerIgnoredProfile ? "Odblokuj" : "Zablokuj") + " " + profile.getName()))
                    .lore(ComponentUtil.asList(
                            "",
                            this.circle + " <light_purple>Kliknij<dark_gray>, <gray>aby " + (playerIgnoredProfile ? StringUtil.getGreenGradient() + "odblokować" : StringUtil.getRedGradient() + "zablokować") + " <gray>gracza<dark_gray>.",
                            ""
                    ))
                    .asGuiItem(event -> {
                        Bukkit.dispatchCommand(player, "ignore " + profile.getName());
                        this.open(player, profile);
                    });
        }

        profile.getGuild().ifPresent(guild -> {
            boolean isLeader = guild.isLeader(profile.getUuid());
            boolean isCoLeader = guild.isCoLeader(profile.getUuid());

            GuiItem guildItem = ItemBuilder.from(Material.BEACON)
                    .name(ComponentUtil.text(me.dmk.core.util.string.StringUtil.getPurpleGradient() + guild.getTag()))
                    .lore(ComponentUtil.asList(
                            "",
                            this.circle + " <gray>Gracz <light_purple>" + profile.getName() + " <gray>jest " + (isLeader ? "<red>liderem" : isCoLeader ? "<yellow>zastępcą lidera" : "<light_purple>członkiem") + " <gray>w gildii <light_purple>" + guild.getTag() + "<dark_gray>.",
                            "",
                            this.circle + " <light_purple>Kilknij LPM<dark_gray>, <gray>aby przejść do panelu tej gildii<dark_gray>.",
                            ""
                    ))
                    .asGuiItem(event ->
                            new GuildPanelGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile, guild)
                    );

            gui.setItem(40, guildItem);
        });

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());

        gui.setItem(13, headItem);
        gui.setItem(21, statisticsItem);
        gui.setItem(22, kitItem);
        gui.setItem(30, punishmentsItem);
        gui.setItem(31, settingsOrIgnoreItem);

        gui.open(player);
    }
}

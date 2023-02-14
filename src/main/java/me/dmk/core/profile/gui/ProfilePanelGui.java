package me.dmk.core.profile.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.AllArgsConstructor;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.gui.item.builder.BarrierBuilder;
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
import me.dmk.core.util.StyleUtil;
import me.dmk.core.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Optional;

/**
 * Created by DMK on 17.01.2023
 */

@AllArgsConstructor
public class ProfilePanelGui {

    private final PluginConfiguration pluginConfiguration;
    private final LuckPermsController luckPermsController;
    private final ProfileController profileController;
    private final ProfileCache profileCache;
    private final GuildCache guildCache;

    public void open(Player player, Profile profile) {
        boolean isSelf = player.getUniqueId().equals(profile.getUuid());

        ProfileSettings settings = profile.getProfileSettings();
        ProfileStatistics statistics = profile.getProfileStatistics();

        Optional<String> group = this.luckPermsController.getOrElseLoad(profile.getUuid())
                .flatMap(u -> this.luckPermsController.getHighestGroupDisplayNameOrName(profile.getUuid()));

        String circle = StyleUtil.getCircle();
        String purpleGradient = StyleUtil.getPurpleGradient();

        Gui gui = Gui.gui()
                .title(ComponentUtil.text(circle + " <light_purple>Panel gracza " + circle))
                .rows(6)
                .disableAllInteractions()
                .create();

        int headSlot = 13;
        int statisticsSlot = 21;
        int kitSlot = 22;
        int punishmentsSlot = 30;
        int settingsOrIgnoreSlot = 31;
        int guildSlot = 40;

        long lastSeenDays = TimeUtil.toDays(profile.getLastJoin().toInstant(), false);
        int timeSpent = statistics.getTimeSpent();

        String lastSeen = lastSeenDays == 0L ? "dzisiaj o " + TimeUtil.formatTime(profile.getLastJoin().toInstant()) : lastSeenDays + " dni temu";
        String timePlayed = TimeUtil.durationToString(Duration.ofSeconds(timeSpent));

        GuiItem headItem = SkullStorage
                .createPlayerHead(profile.getUuid())
                .name(ComponentUtil.text(settings.getColorName().getFormat() + profile.getName() + " " + settings.getCustomSuffix().getFormat()))
                .lore(ComponentUtil.asList(
                        "",
                        circle + " <gray>Online<dark_gray>: " + (profile.getPlayer().isPresent() ? "<green>Tak" : "<red>Nie <dark_gray>- <gray>Ostatnio widziany<dark_gray>: <light_purple>" + lastSeen + "<dark_gray>."),
                        "",
                        circle + " <gray>Grupa<dark_gray>: <light_purple>" + group.orElse("Brak"),
                        circle + " <gray>Założenie konta<dark_gray>: <light_purple>" + TimeUtil.format(profile.getFirstJoin().toInstant()),
                        circle + " <gray>Karany<dark_gray>: " + (profile.getPunishments().isEmpty() ? "<green>Nie" : "<red>Tak"),
                        circle + " <gray>Odwiedził nas <light_purple>" + statistics.getEntrances() + " razy <green>" + StyleUtil.getSmile(),
                        ""
                ))
                .asGuiItem();

        GuiItem statisticsItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(purpleGradient + "Statystyki"))
                .lore(ComponentUtil.asList(
                        "",
                        circle + " <gray>Poziom doświadczenia<dark_gray>: <light_purple>" + statistics.getLevel(),
                        circle + " <gray>Monety<dark_gray>: <light_purple>" + statistics.getCoins(),
                        circle + " <gray>Spędzony czas<dark_gray>: <light_purple>" + timePlayed,
                        "",
                        circle + " <gray>Punkty<dark_gray>: <light_purple>" + statistics.getPoints(),
                        circle + " <gray>Zabójstwa<dark_gray>: <light_purple>" + statistics.getKills(),
                        circle + " <gray>Aktualna seria zabójstw<dark_gray>: <light_purple>" + statistics.getKillStreak(),
                        circle + " <gray>Najwyższa seria zabójstw<dark_gray>: <light_purple>" + statistics.getHighestKillStreak(),
                        circle + " <gray>Śmierci<dark_gray>: <light_purple>" + statistics.getDeaths(),
                        "" + (isSelf ? "<!italic>" + StyleUtil.getWarning() + " <light_purple>Kilknij<dark_gray>, <gray>aby <light_purple>zresetować <gray>swoje statystyki<dark_gray>." : null)
                ))
                .asGuiItem(event -> {
                    if (!isSelf) {
                        return;
                    }

                    int coinsToResetStatistics = this.pluginConfiguration.getCoinsToResetStatistics();

                    if (statistics.getCoins() > coinsToResetStatistics) {
                        new BarrierBuilder()
                                .name("<red>Nie spełniasz wymagań")
                                .lore(StyleUtil.getWarning() + " <red>Aby zresetować statystyki potrzebujesz <gold>" + coinsToResetStatistics + " <red>monet<dark_gray>.")
                                .updateGui(gui, event.getSlot());
                        return;
                    }

                    Bukkit.dispatchCommand(player, "resetstatistics");
                });

        GuiItem kitItem = ItemBuilder.from(Material.SHIELD)
                .name(ComponentUtil.text(purpleGradient + "Zestaw"))
                .lore(ComponentUtil.asList(
                        "",
                        circle + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do poglądu zestawu<dark_gray>.",
                        ""
                ))
                .asGuiItem();

        GuiItem punishmentsItem = ItemBuilder.from(Material.TARGET)
                        .name(ComponentUtil.text(purpleGradient + "Historia kar"))
                        .lore(ComponentUtil.asList(
                                "",
                                circle + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do historii kar<dark_gray>.",
                                ""
                        ))
                        .asGuiItem(event -> {
                            if (profile.getPunishments().isEmpty()) {
                                new BarrierBuilder()
                                        .name("<green>Gracz nie posiada historii kar")
                                        .lore(
                                                "",
                                                circle + " <gray>Zajrzyj tutaj innym razem <green>" + StyleUtil.getSmile(),
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
                    .name(ComponentUtil.text(purpleGradient + "Ustawienia"))
                    .lore(ComponentUtil.asList(
                            "",
                            circle + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do ustawień profilu<dark_gray>.",
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
                    .name(ComponentUtil.text(purpleGradient + (playerIgnoredProfile ? "Odblokuj" : "Zablokuj") + " " + profile.getName()))
                    .lore(ComponentUtil.asList(
                            "",
                            circle + " <light_purple>Kliknij<dark_gray>, <gray>aby " + (playerIgnoredProfile ? StyleUtil.getGreenGradient() + "odblokować" : StyleUtil.getRedGradient() + "zablokować") + " <gray>gracza<dark_gray>.",
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
                    .name(ComponentUtil.text(purpleGradient + guild.getTag()))
                    .lore(ComponentUtil.asList(
                            "",
                            circle + " <gray>Gracz <light_purple>" + profile.getName() + " <gray>jest " + (isLeader ? "<red>liderem" : isCoLeader ? "<yellow>zastępcą lidera" : "<light_purple>członkiem") + " <gray>w gildii <light_purple>" + guild.getTag() + "<dark_gray>.",
                            "",
                            circle + " <light_purple>Kilknij LPM<dark_gray>, <gray>aby przejść do panelu tej gildii<dark_gray>.",
                            ""
                    ))
                    .asGuiItem(event ->
                            new GuildPanelGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile, guild)
                    );

            gui.setItem(guildSlot, guildItem);
        });

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());

        gui.setItem(headSlot, headItem);
        gui.setItem(statisticsSlot, statisticsItem);
        gui.setItem(kitSlot, kitItem);
        gui.setItem(punishmentsSlot, punishmentsItem);
        gui.setItem(settingsOrIgnoreSlot, settingsOrIgnoreItem);

        gui.open(player);
    }
}

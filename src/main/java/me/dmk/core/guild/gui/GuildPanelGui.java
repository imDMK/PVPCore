package me.dmk.core.guild.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.AllArgsConstructor;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.gui.item.builder.BarrierBuilder;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.guild.member.GuildMembersGui;
import me.dmk.core.guild.statistics.GuildStatistics;
import me.dmk.core.guild.treasury.GuildTreasury;
import me.dmk.core.guild.treasury.GuildTreasuryGui;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.PlayerUtil;
import me.dmk.core.util.StyleUtil;
import me.dmk.core.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by DMK on 19.01.2023
 */

@AllArgsConstructor
public class GuildPanelGui {

    private final PluginConfiguration pluginConfiguration;
    private final LuckPermsController luckPermsController;
    private final ProfileController profileController;
    private final ProfileCache profileCache;
    private final GuildCache guildCache;

    public void open(Player player, Profile profile, Guild guild) {
        String circle = StyleUtil.getCircle();
        String purpleGradient = StyleUtil.getPurpleGradient();

        Gui gui = Gui.gui()
                .title(ComponentUtil.text(circle + " <light_purple>Panel gildyjny " + circle))
                .rows(6)
                .disableAllInteractions()
                .create();

        GuildStatistics guildStatistics = guild.getGuildStatistics();
        GuildTreasury guildTreasury = guild.getGuildTreasury();

        boolean isLeader = guild.isLeader(player.getUniqueId());
        boolean isMember = guild.isMember(player.getUniqueId());

        String creatorName = Optional.ofNullable(Bukkit.getPlayer(guild.getCreator()))
                .map(Player::getName)
                .orElse("Brak");

        String leader = Optional.ofNullable(Bukkit.getPlayer(guild.getLeader()))
                .map(Player::getName)
                .orElse("Brak");

        String coLeader = Optional.ofNullable(Bukkit.getPlayer(guild.getCoLeader()))
                .map(Player::getName)
                .orElse("Brak");

        GuiItem beaconItem = ItemBuilder.from(Material.BEACON)
                .name(ComponentUtil.text(purpleGradient + guild.getTag()))
                .lore(ComponentUtil.asList(
                        "",
                        circle + " <gray>Nazwa<dark_gray><dark_gray>: <light_purple>" + guild.getName(),
                        circle + " <gray>Założyciel<dark_gray>: <light_purple>" + creatorName,
                        circle + " <gray>Data założenia<dark_gray>: <light_purple>" + TimeUtil.format(guild.getCreatedAt().toInstant()),
                        "",
                        circle + " <gray>Lider<dark_gray>: <light_purple>" + leader,
                        circle + " <gray>Zastępca lidera<dark_gray>: <light_purple>" + coLeader,
                        ""
                ))
                .asGuiItem();

        GuiItem statisticsItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(purpleGradient + "Statystyki"))
                .lore(ComponentUtil.asList(
                        "",
                        circle + " <gray>Ranking<dark_gray>: <light_purple>" + guildStatistics.getRank(),
                        circle + " <gray>Zabójstwa<dark_gray>: <light_purple>" + guildStatistics.getKills(),
                        circle + " <gray>Seria zabójstw<dark_gray>: <light_purple>" + guildStatistics.getKillStreak(),
                        circle + " <gray>Największa seria zabójstw<dark_gray>: <light_purple>" + guildStatistics.getHighestKillStreak(),
                        circle + " <gray>Śmierci<dark_gray>: <light_purple>" + guildStatistics.getDeaths(),
                        circle + " <gray>KDR<dark_gray>: <light_purple>" + PlayerUtil.getKDR(guildStatistics.getKills(), guildStatistics.getDeaths()),
                        ""
                ))
                .asGuiItem();

        GuiItem expireItem = ItemBuilder.from(Material.REDSTONE_TORCH)
                .name(ComponentUtil.text(purpleGradient + "Wygasa"))
                .lore(ComponentUtil.asList(
                        "",
                        circle + " <gray>Gildia wygasa za<dark_gray>: <red>" + TimeUtil.instantToString(guild.getExpireAt().toInstant(), true),
                        ""
                ))
                .asGuiItem();

        GuiItem membersItem = ItemBuilder.from(Material.PLAYER_HEAD)
                .name(ComponentUtil.text(purpleGradient + "Członkowie"))
                .lore(ComponentUtil.asList(
                        "",
                        StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do listy członków gildii<dark_gray>.",
                        ""
                ))
                .asGuiItem(event ->
                        new GuildMembersGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile, guild)
                );

        GuiItem coinsTrasureItem = ItemBuilder.from(Material.SUNFLOWER)
                .name(ComponentUtil.text(purpleGradient + "Skarbiec gildijny"))
                .lore(ComponentUtil.asList(
                        "",
                        isMember ? StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do menu skarbca gildyjnego<dark_gray>." : circle + " <gray>Gildia posiada <light_purple>" + guildTreasury.getCoins() + " <gray>monet w skarbcu<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    if (!isMember) {
                        return;
                    }

                    new GuildTreasuryGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile, guild);
                });

        GuiItem alliancesItem = ItemBuilder.from(Material.SHIELD)
                .name(ComponentUtil.text(purpleGradient + "Sojusznicy"))
                .lore(ComponentUtil.asList(
                        "",
                        StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do listy sojuszów gildii<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    if (guild.getAlliances().isEmpty()) {
                        new BarrierBuilder()
                                .name("<red>Gildia nie posiada sojuszów<dark_gray>.")
                                .updateGui(gui, event.getSlot());
                        return;
                    }

                    new GuildAllianceGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile, guild);
                });

        GuiItem leaveOrDeleteGuildItem;
        if (isLeader) {
            leaveOrDeleteGuildItem = ItemBuilder.from(Material.OAK_DOOR)
                    .name(ComponentUtil.text(purpleGradient + "Usuń gildię"))
                    .lore(ComponentUtil.asList(
                            "",
                            StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby <red>usunąć gildię<dark_gray>.",
                            ""
                    ))
                    .asGuiItem(event -> Bukkit.dispatchCommand(player, "guild delete"));
        } else {
            leaveOrDeleteGuildItem = ItemBuilder.from(Material.OAK_DOOR)
                    .name(ComponentUtil.text(purpleGradient + "Opuść gildię"))
                    .lore(ComponentUtil.asList(
                            "",
                            StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby <red>opuścić gildię<dark_gray>.",
                            ""
                    ))
                    .asGuiItem(event -> Bukkit.dispatchCommand(player, "guild leave"));
        }

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());

        gui.setItem(13, beaconItem);

        gui.setItem(21, statisticsItem);
        gui.setItem(22, expireItem);
        gui.setItem(23, membersItem);

        gui.setItem(31, coinsTrasureItem);
        gui.setItem(32, alliancesItem);

        gui.setItem(40, leaveOrDeleteGuildItem);

        gui.open(player);
    }
}

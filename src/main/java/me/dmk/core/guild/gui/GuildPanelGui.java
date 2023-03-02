package me.dmk.core.guild.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.gui.item.builder.BarrierBuilder;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.member.GuildMemberListGui;
import me.dmk.core.guild.statistics.GuildStatistics;
import me.dmk.core.guild.treasury.GuildTreasury;
import me.dmk.core.guild.treasury.GuildTreasuryGui;
import me.dmk.core.profile.Profile;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.PlayerUtil;
import me.dmk.core.util.TimeUtil;
import me.dmk.core.util.string.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by DMK on 02.03.2023
 */

public class GuildPanelGui extends PluginGui {

    public final Guild guild;

    public GuildPanelGui(Player player, Profile profile, Guild guild) {
        super(player, profile, "Panel gildyjny", 6, true, true);

        this.guild = guild;
    }

    @Override
    public void build() {
        GuildStatistics guildStatistics = this.guild.getGuildStatistics();
        GuildTreasury guildTreasury = this.guild.getGuildTreasury();

        boolean isLeader = this.guild.isLeader(this.player.getUniqueId());
        boolean isMember = this.guild.isMember(this.player.getUniqueId());

        String creatorName = Optional.ofNullable(Bukkit.getPlayer(this.guild.getCreator()))
                .map(Player::getName)
                .orElse("Brak");

        String leader = Optional.ofNullable(Bukkit.getPlayer(this.guild.getLeader()))
                .map(Player::getName)
                .orElse("Brak");

        String coLeader = Optional.ofNullable(Bukkit.getPlayer(this.guild.getCoLeader()))
                .map(Player::getName)
                .orElse("Brak");

        GuiItem beaconItem = ItemBuilder.from(Material.BEACON)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + this.guild.getTag()))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Nazwa<dark_gray><dark_gray>: <light_purple>" + guild.getName(),
                        this.circle + " <gray>Założyciel<dark_gray>: <light_purple>" + creatorName,
                        this.circle + " <gray>Data założenia<dark_gray>: <light_purple>" + TimeUtil.formatDate(this.guild.getCreatedAt().toInstant()),
                        "",
                        this.circle + " <gray>Lider<dark_gray>: <light_purple>" + leader,
                        this.circle + " <gray>Zastępca lidera<dark_gray>: <light_purple>" + coLeader,
                        ""
                ))
                .asGuiItem();

        GuiItem statisticsItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Statystyki"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Ranking<dark_gray>: <light_purple>" + guildStatistics.getRank(),
                        this.circle + " <gray>Zabójstwa<dark_gray>: <light_purple>" + guildStatistics.getKills(),
                        this.circle + " <gray>Seria zabójstw<dark_gray>: <light_purple>" + guildStatistics.getKillStreak(),
                        this.circle + " <gray>Największa seria zabójstw<dark_gray>: <light_purple>" + guildStatistics.getHighestKillStreak(),
                        this.circle + " <gray>Śmierci<dark_gray>: <light_purple>" + guildStatistics.getDeaths(),
                        this.circle + " <gray>KDR<dark_gray>: <light_purple>" + PlayerUtil.getKDR(guildStatistics.getKills(), guildStatistics.getDeaths()),
                        ""
                ))
                .asGuiItem();

        GuiItem expireItem = ItemBuilder.from(Material.REDSTONE_TORCH)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Wygasa"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Gildia wygasa za<dark_gray>: <red>" + TimeUtil.instantToString(this.guild.getExpireAt().toInstant(), true),
                        ""
                ))
                .asGuiItem();

        GuiItem membersItem = ItemBuilder.from(Material.PLAYER_HEAD)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Członkowie"))
                .lore(ComponentUtil.asList(
                        "",
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do listy członków gildii<dark_gray>.",
                        ""
                ))
                .asGuiItem(event ->
                        new GuildMemberListGui(this.player, this.profile, this.guild).open()
                );

        GuiItem coinsTrasureItem = ItemBuilder.from(Material.SUNFLOWER)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Skarbiec gildijny"))
                .lore(ComponentUtil.asList(
                        "",
                        isMember ? this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do menu skarbca gildyjnego<dark_gray>." : this.circle + " <gray>Gildia posiada <light_purple>" + guildTreasury.getCoins() + " <gray>monet w skarbcu<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    if (!isMember) {
                        return;
                    }

                    new GuildTreasuryGui(this.player, this.profile, this.guild).open();
                });

        GuiItem alliancesItem = ItemBuilder.from(Material.SHIELD)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Sojusznicy"))
                .lore(ComponentUtil.asList(
                        "",
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do listy sojuszów gildii<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    if (this.guild.getAlliances().isEmpty()) {
                        new BarrierBuilder()
                                .name("<red>Gildia nie posiada sojuszów<dark_gray>.")
                                .updateItem(gui, event.getSlot());
                        return;
                    }

                    new GuildAllianceListGui(this.player, this.profile, this.guild).open();
                });

        GuiItem leaveOrDeleteGuildItem;
        if (isLeader) {
            leaveOrDeleteGuildItem = ItemBuilder.from(Material.OAK_DOOR)
                    .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Usuń gildię"))
                    .lore(ComponentUtil.asList(
                            "",
                            this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby <red>usunąć gildię<dark_gray>.",
                            ""
                    ))
                    .asGuiItem(event ->
                            Bukkit.dispatchCommand(player, "guild delete")
                    );
        } else {
            leaveOrDeleteGuildItem = ItemBuilder.from(Material.OAK_DOOR)
                    .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Opuść gildię"))
                    .lore(ComponentUtil.asList(
                            "",
                            this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby <red>opuścić gildię<dark_gray>.",
                            ""
                    ))
                    .asGuiItem(event ->
                            Bukkit.dispatchCommand(this.player, "guild leave")
                    );
        }

        this.gui.setItem(13, beaconItem);

        this.gui.setItem(21, statisticsItem);
        this.gui.setItem(22, expireItem);
        this.gui.setItem(23, membersItem);

        this.gui.setItem(31, coinsTrasureItem);
        this.gui.setItem(32, alliancesItem);

        this.gui.setItem(40, leaveOrDeleteGuildItem);
    }
}

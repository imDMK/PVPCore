package me.dmk.core.guild.member.gui;

import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.CorePlugin;
import me.dmk.core.gui.PluginPaginatedGui;
import me.dmk.core.gui.confirmation.ConfirmationGui;
import me.dmk.core.gui.item.builder.BarrierBuilder;
import me.dmk.core.gui.item.storage.SkullStorage;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.gui.GuildPanelGui;
import me.dmk.core.guild.member.GuildMember;
import me.dmk.core.guild.rank.GuildRank;
import me.dmk.core.guild.rank.gui.GuildRankListGui;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.gui.ProfilePanelGui;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created by DMK on 02.03.2023
 */

public class GuildMemberListGui extends PluginPaginatedGui {

    private final ProfileCache profileCache = CorePlugin.getCorePlugin().getProfileCache();

    public final Guild guild;

    public GuildMemberListGui(Player player, Profile profile, Guild guild) {
        super(player, profile, "Lista członków", 6, true, true);

        this.guild = guild;
    }

    @Override
    public void build() {
        Collection<GuildMember> guildMemberList = this.guild.getMembers().values()
                .stream()
                .sorted(Comparator.comparingInt(i -> this.guild.getGuildRank(i.getGuildRankUuid()).getPriority()))
                .toList();

        boolean canManageMembers = this.guild.getGuildRank(this.player.getUniqueId()).isCanManageMembers();

        GuiItem previousButton = this.createPreviousPageButton(this.gui);
        GuiItem backButton = this.createBackButton(event ->
                        new GuildPanelGui(this.player, this.profile, this.guild).open(),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do panelu gildii<dark_gray>.",
                ""
        );
        GuiItem nextButton = this.createNextPageButton(this.gui);

        this.gui.setItem(47, previousButton);
        this.gui.setItem(49, backButton);
        this.gui.setItem(51, nextButton);

        for (GuildMember guildMember : guildMemberList) {
            boolean isSelf = this.player.getUniqueId().equals(guildMember.getUuid());

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(guildMember.getUuid());
            GuildRank guildRank = this.guild.getGuildRank(guildMember.getGuildRankUuid());

            List<String> memberItemLore = new ArrayList<>(Arrays.asList(
                    "",
                    this.circle + " <gray>Ranga gildyjna<dark_gray>: <light_purple>" + guildRank.getName(),
                    this.circle + " <gray>Data dołączenia<dark_gray>: <light_purple>" + TimeUtil.formatDate(guildMember.getJoinDate().toInstant()),
                    "",
                    this.warning + " <light_purple>Kliknij LPM<dark_gray>, <gray>aby otworzyć profil tego gracza<dark_gray>.",
                    ""
            ));

            if (canManageMembers) {
                memberItemLore.addAll(Arrays.asList(
                        this.warning + " <light_purple>Kilknij LPM + SHIFT<dark_gray>, <gray>aby <red>zarządzać rangami <gray>gracza gildii<dark_gray>.",
                        this.warning + " <light_purple>Kilknij PPM + SHIFT<dark_gray>, <gray>aby <red>wyrzucić <gray>gracza z gildii<dark_gray>.",
                        ""
                ));
            }

            GuiItem memberItem = SkullStorage.createPlayerHead(offlinePlayer.getUniqueId())
                    .name(ComponentUtil.text("<light_purple>" + offlinePlayer.getName()))
                    .lore(ComponentUtil.asList(memberItemLore))
                    .asGuiItem(event -> {
                        if (event.isLeftClick() && !event.isShiftClick()) {
                            this.profileCache.getOrElseLoad(guildMember.getUuid())
                                    .ifPresent(memberProfile ->
                                            new ProfilePanelGui(this.player, memberProfile).open()
                                    );
                            return;
                        }

                        if (!canManageMembers) {
                            return;
                        }

                        if (event.isShiftClick()) {
                            if (event.isLeftClick()) {
                                GuildRankListGui guildRankListGui = new GuildRankListGui(this.player, this.profile, this.guild);
                                guildRankListGui.build();

                                guildRankListGui.gui.setDefaultClickAction(e -> {
                                    ItemStack itemStack = e.getCurrentItem();
                                    if (itemStack == null) {
                                        return;
                                    }

                                    if (itemStack.getItemMeta() instanceof GuiItem guiItem) {
                                        System.out.println(1);
                                        System.out.println(guiItem.getItemStack().getItemMeta().getDisplayName());
                                    }
                                });

                                guildRankListGui.open(false);
                                return;
                            }

                            if (event.isRightClick()) {
                                if (isSelf) {
                                    new BarrierBuilder()
                                            .name("<red>Zwariowałeś? Nie możesz wyrzucić samego siebie<dark_gray>...")
                                            .lore(this.warning + " <green>Aby opuścić gildię, użyj komendy /guild leave")
                                            .updateItem(this.gui, event.getSlot());
                                    return;
                                }

                                if (this.guild.isLeader(guildMember.getUuid())) {
                                    new BarrierBuilder()
                                            .name("<red>Gracz pełni funkcję lidera gildii<dark_gray>.")
                                            .updateItem(this.gui, event.getSlot());
                                    return;
                                }

                                new ConfirmationGui(this.player)
                                        .title("Potwierdź wyrzucenie " + offlinePlayer.getName())
                                        .afterConfirm(e -> {
                                            Bukkit.dispatchCommand(this.player, "guild kick " + offlinePlayer.getName());
                                            this.open();
                                        })
                                        .afterCancel(e -> this.open())
                                        .open();
                            }
                        }
                    });

            this.gui.addItem(memberItem);
        }
    }
}

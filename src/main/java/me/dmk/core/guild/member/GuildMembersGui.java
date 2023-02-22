package me.dmk.core.guild.member;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.gui.confirmation.ConfirmationGui;
import me.dmk.core.gui.item.builder.BarrierBuilder;
import me.dmk.core.gui.item.storage.SkullStorage;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.gui.GuildPanelGui;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.gui.ProfilePanelGui;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Comparator;

/**
 * Created by DMK on 28.01.2023
 */

public class GuildMembersGui extends PluginGui {

    public void open(Player player, Profile profile, Guild guild) {
        PaginatedGui gui = Gui.paginated()
                .title(ComponentUtil.text(this.circle + "<light_purple>Lista członków " + this.circle))
                .rows(6)
                .disableAllInteractions()
                .create();

        Collection<GuildMember> guildMemberList = guild.getMembers().values()
                .stream()
                .sorted(Comparator.comparing(GuildMember::getJoinDate).reversed())
                .toList();

        boolean isLeaderOrCoLeader = guild.isLeaderOrCoLeader(player.getUniqueId());

        GuiItem previousButton = this.createPreviousPageButton(gui);
        GuiItem backButton = this.createBackButton(event ->
                        new GuildPanelGui().open(player, profile, guild),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do panelu gildii<dark_gray>.",
                ""
        );
        GuiItem nextButton = this.createNextPageButton(gui);

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());

        gui.setItem(47, previousButton);
        gui.setItem(49, backButton);
        gui.setItem(51, nextButton);

        for (GuildMember guildMember : guildMemberList) {
            boolean memberIsCreator = guild.isCreator(guildMember.getUuid());
            boolean memberIsLeader = guild.isLeader(guildMember.getUuid());
            boolean memberIsCoLeader = guild.isCoLeader(guildMember.getUuid());

            boolean isSelf = player.getUniqueId().equals(guildMember.getUuid());

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(guildMember.getUuid());

            GuiItem memberItem = SkullStorage.createPlayerHead(offlinePlayer)
                    .name(ComponentUtil.text("<light_purple>" + offlinePlayer.getName()))
                    .lore(ComponentUtil.asList(
                            "",
                            this.circle + " <gray>Ranga gildyjna<dark_gray>: <light_purple>" + (memberIsCreator ? "Założyciel" : memberIsLeader ? "Lider" : memberIsCoLeader ? "Zastępca lidera" : "Członek"),
                            this.circle + " <gray>Data dołączenia<dark_gray>: <light_purple>" + TimeUtil.format(guildMember.getJoinDate().toInstant()),
                            "",
                            this.warning + " <light_purple>Kliknij LPM<dark_gray>, <gray>aby otworzyć profil tego gracza<dark_gray>.",
                            (isLeaderOrCoLeader ? "<!italic>" + this.warning + " <light_purple>Kilknij SHIFT + PPM<dark_gray>, <gray>aby <red>wyrzucić <gray>gracza z gildii<dark_gray>." : null),
                            ""
                    ))
                    .glow(memberIsCoLeader || memberIsLeader || memberIsCreator)
                    .asGuiItem(event -> {
                        if (event.isLeftClick()) {
                            this.profileCache.getOrElseLoad(guildMember.getUuid())
                                    .ifPresent(memberProfile ->
                                            new ProfilePanelGui()
                                                    .open(player, memberProfile)
                                    );
                            return;
                        }

                        if (isLeaderOrCoLeader && event.isRightClick() && event.isRightClick()) {
                            if (isSelf) {
                                new BarrierBuilder()
                                        .name("<red>Zwariowałeś? Nie możesz wyrzucić samego siebie<dark_gray>...")
                                        .lore(
                                                this.warning + " <green>Aby opuścić gildię, użyj komendy /guild leave"
                                        )
                                        .updateGui(gui, event.getSlot());
                                return;
                            }

                            if (guild.isLeader(guildMember.getUuid())) {
                                new BarrierBuilder()
                                        .name("<red>Gracz pełni funkcję lidera gildii<dark_gray>.")
                                        .updateGui(gui, event.getSlot());
                                return;
                            }

                            new ConfirmationGui(player)
                                    .create(this.circle + " <light_purple>Wrzucenie " + offlinePlayer.getName() + " " + this.circle)
                                    .afterConfirm(e -> {
                                        Bukkit.dispatchCommand(player, "guild kick " + offlinePlayer.getName());
                                        this.open(player, profile, guild);
                                    })
                                    .afterCancel(e -> this.open(player, profile, guild))
                                    .open();
                        }
                    });

            gui.addItem(memberItem);
        }

        gui.open(player);
    }
}

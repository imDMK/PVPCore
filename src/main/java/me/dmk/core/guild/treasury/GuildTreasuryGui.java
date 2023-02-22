package me.dmk.core.guild.treasury;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.gui.item.builder.BarrierBuilder;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.gui.GuildPanelGui;
import me.dmk.core.guild.treasury.payment.GuildPaymentsGui;
import me.dmk.core.guild.treasury.payment.GuildTopPaymentsGui;
import me.dmk.core.profile.Profile;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.string.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 01.02.2023
 */

public class GuildTreasuryGui extends PluginGui {

    public void open(Player player, Profile profile, Guild guild) {
        Gui gui = Gui.gui()
                .title(ComponentUtil.text(this.circle + " <light_purple>Skarbiec gildyjny " + this.circle))
                .rows(3)
                .disableAllInteractions()
                .create();

        GuildTreasury guildTreasury = guild.getGuildTreasury();

        boolean isLeaderOrCoLeader = guild.isLeaderOrCoLeader(player.getUniqueId());

        int coinsToExtendGuild = this.pluginConfiguration.getCoinsToExtendGuild();
        boolean guildCanExtend = guildTreasury.getCoins() > coinsToExtendGuild;
        boolean canExtend = profile.getProfileStatistics().getCoins() > coinsToExtendGuild;

        GuiItem backButton = this.createBackButton(event ->
                        new GuildPanelGui().open(player, profile, guild),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do panelu gildii<dark_gray>.",
                ""
        );

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());
        gui.setItem(22, backButton);

        GuiItem topsItem = ItemBuilder.from(Material.GLOW_ITEM_FRAME)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Topka wpłaconych monet"))
                .lore(ComponentUtil.asList(
                        "",
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do topki wpłaconych monet<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    if (guildTreasury.getGuildPayments().isEmpty()) {
                        new BarrierBuilder()
                                .name("<red>Brak osób będących w topce<dark_gray>.")
                                .updateGui(gui, event.getSlot());
                        return;
                    }

                    new GuildTopPaymentsGui().open(player, profile, guild);
                });

        GuiItem coinsItem = ItemBuilder.from(Material.SUNFLOWER)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Monety"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualnie w skarbcu gildyjnym znajduje się <light_purple>" + guildTreasury.getCoins() + " <gray>monet<dark_gray>.",
                        this.circle + " <gray>Możliwość przedłużenia gildii<dark_gray>: " + (guildCanExtend || canExtend ? (isLeaderOrCoLeader ? "<green>Tak - Kliknij, aby przedłużyć" : "<green>Tak") : "<red>Nie"),
                        ""
                ))
                .asGuiItem(event -> {
                    if (canExtend && isLeaderOrCoLeader) {
                        Bukkit.dispatchCommand(player, "guild extend");
                    }
                });

        GuiItem guildPaymentsHistoryItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(StringUtil.getPurpleGradient() + "Historia wpłaconych monet"))
                .lore(ComponentUtil.asList(
                        "",
                        this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby przejść do topki wpłaconych monet<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    if (guildTreasury.getGuildPayments().isEmpty()) {
                        new BarrierBuilder()
                                .name("<red>Brak historii<dark_gray>.")
                                .updateGui(gui, event.getSlot());
                        return;
                    }

                    new GuildPaymentsGui().open(player, profile, guild);
                });

        gui.setItem(12, topsItem);
        gui.setItem(13, coinsItem);
        gui.setItem(14, guildPaymentsHistoryItem);

        gui.open(player);
    }
}

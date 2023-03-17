package me.dmk.core.guild.treasury;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.CorePlugin;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.gui.item.builder.BarrierBuilder;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.gui.GuildPanelGui;
import me.dmk.core.guild.treasury.payment.GuildPaymentsGui;
import me.dmk.core.guild.treasury.payment.GuildTopPaymentsGui;
import me.dmk.core.profile.Profile;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 02.03.2023
 */

public class GuildTreasuryGui extends PluginGui {

    private final PluginConfiguration pluginConfiguration = CorePlugin.getCorePlugin().getPluginConfiguration();

    private final Profile profile;
    private final Guild guild;

    public GuildTreasuryGui(Player player, Profile profile, Guild guild) {
        super(player, "Skarbiec gildyjny", 3, true, true);

        this.profile = profile;
        this.guild = guild;
    }

    @Override
    public void build() {
        GuildTreasury guildTreasury = this.guild.getGuildTreasury();

        int coinsToExtendGuild = this.pluginConfiguration.getCoinsToExtendGuild();

        boolean guildCanExtend = guildTreasury.getCoins() > coinsToExtendGuild;
        boolean playerHasPermission = this.guild.getGuildRank(this.player.getUniqueId()).isCanExtend();
        boolean playerCanExtend = this.profile.getProfileStatistics().getCoins() > coinsToExtendGuild;

        GuiItem topsItem = ItemBuilder.from(Material.GLOW_ITEM_FRAME)
                .name(ComponentUtil.text(StringFormatter.formatPurpleGradient() + "Topka wpłaconych monet"))
                .lore(ComponentUtil.asList(
                        "",
                        this.warning + " <gold>Kliknij<dark_gray>, <gray>aby przejść do topki wpłaconych monet<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    if (guildTreasury.getGuildPayments().isEmpty()) {
                        new BarrierBuilder()
                                .name("<red>Brak osób będących w topce<dark_gray>.")
                                .updateItem(this.gui, event.getSlot());
                        return;
                    }

                    new GuildTopPaymentsGui(this.player, this.profile, this.guild).open();
                });

        GuiItem coinsItem = ItemBuilder.from(Material.SUNFLOWER)
                .name(ComponentUtil.text(StringFormatter.formatPurpleGradient() + "Monety"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Aktualnie w skarbcu gildyjnym znajduje się <light_purple>" + guildTreasury.getCoins() + " <gray>monet<dark_gray>.",
                        this.circle + " <gray>Możliwość przedłużenia gildii<dark_gray>: " + (guildCanExtend || playerCanExtend ? (playerHasPermission ? "<green>Tak - Kliknij, aby przedłużyć" : "<green>Tak") : "<red>Nie"),
                        ""
                ))
                .asGuiItem(event -> {
                    if (!playerHasPermission) {
                        new BarrierBuilder()
                                .name("<red>Nie posiadasz uprawnień gildyjnych.")
                                .updateItem(this.gui, event.getSlot());
                        return;
                    }

                    if (guildCanExtend || playerCanExtend) {
                        Bukkit.dispatchCommand(this.player, "guild extend");
                    }
                });

        GuiItem guildPaymentsHistoryItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text(StringFormatter.formatPurpleGradient() + "Historia wpłaconych monet"))
                .lore(ComponentUtil.asList(
                        "",
                        this.warning + " <gold>Kliknij<dark_gray>, <gray>aby przejść do topki wpłaconych monet<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    if (guildTreasury.getGuildPayments().isEmpty()) {
                        new BarrierBuilder()
                                .name("<red>Brak historii<dark_gray>.")
                                .updateItem(this.gui, event.getSlot());
                        return;
                    }

                    new GuildPaymentsGui(this.player, this.profile, this.guild).open();
                });

        GuiItem backButton = this.createBackButton(event ->
                        new GuildPanelGui(this.player, this.profile, this.guild).open(),
                "",
                this.warning + " <gold>Kliknij<dark_gray>, <gray>aby powrócić do panelu gildii<dark_gray>.",
                ""
        );

        this.gui.setItem(12, topsItem);
        this.gui.setItem(13, coinsItem);
        this.gui.setItem(14, guildPaymentsHistoryItem);

        this.gui.setItem(22, backButton);
    }
}

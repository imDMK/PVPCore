package me.dmk.core.guild.treasury.payment;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.treasury.GuildTreasuryGui;
import me.dmk.core.profile.Profile;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.TimeUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;

/**
 * Created by DMK on 01.02.2023
 */

public class GuildPaymentsGui extends PluginGui {

    public void open(Player player, Profile profile, Guild guild) {
        PaginatedGui gui = Gui.paginated()
                .title(ComponentUtil.text(this.circle + " <light_purple>Historia wpłat do skarbca " + this.circle))
                .rows(6)
                .disableAllInteractions()
                .create();

        GuiItem previousButton = this.createPreviousPageButton(gui);
        GuiItem backButton = this.createBackButton(event ->
                        new GuildTreasuryGui().open(player, profile, guild),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do menu skarbca gildyjnego<dark_gray>.",
                ""
        );
        GuiItem nextButton = this.createNextPageButton(gui);

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());

        gui.setItem(47, previousButton);
        gui.setItem(49, backButton);
        gui.setItem(51, nextButton);

        List<GuildPayment> guildPaymentList = guild.getGuildTreasury().getGuildPayments()
                .stream()
                .sorted(Comparator.comparing(GuildPayment::getPaymentDate).reversed())
                .toList();

        for (GuildPayment guildPayment : guildPaymentList) {
            GuiItem paymentItem = ItemBuilder.from(Material.SUNFLOWER)
                    .name(ComponentUtil.text("<light_purple>Wpłata " + guildPayment.getPayingName()))
                    .lore(ComponentUtil.asList(
                            "",
                            this.circle + " <gray>Ilość<dark_gray>: <light_purple>" + guildPayment.getAmountCoins(),
                            this.circle + " <gray>Data<dark_gray>: <light_purple>" + TimeUtil.format(guildPayment.getPaymentDate().toInstant()),
                            "",
                            this.circle + " <gray>Saldo skarbca po wpłacie<dark_gray>: <light_purple>" + guildPayment.getBalanceAfterPayment(),
                            ""
                    ))
                    .glow(guildPayment.getAmountCoins() > 1000)
                    .asGuiItem();

            gui.addItem(paymentItem);
        }

        gui.open(player);
    }
}

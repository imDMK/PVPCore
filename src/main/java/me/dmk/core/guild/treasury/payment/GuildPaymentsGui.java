package me.dmk.core.guild.treasury.payment;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginPaginatedGui;
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
 * Created by DMK on 02.03.2023
 */

public class GuildPaymentsGui extends PluginPaginatedGui {

    public final Guild guild;

    public GuildPaymentsGui(Player player, Profile profile, Guild guild) {
        super(player, profile, "Historia wpłat do skarbca", 6, true, true);

        this.guild = guild;
    }

    @Override
    public void build() {
        GuiItem previousButton = this.createPreviousPageButton(this.gui);
        GuiItem backButton = this.createBackButton(event ->
                        new GuildTreasuryGui(this.player, this.profile, this.guild).open(),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do menu skarbca gildyjnego<dark_gray>.",
                ""
        );
        GuiItem nextButton = this.createNextPageButton(this.gui);

        this.gui.setItem(47, previousButton);
        this.gui.setItem(49, backButton);
        this.gui.setItem(51, nextButton);

        List<GuildPayment> guildPaymentList = this.guild.getGuildTreasury().getGuildPayments()
                .stream()
                .sorted(Comparator.comparing(GuildPayment::getPaymentDate).reversed())
                .toList();

        for (GuildPayment guildPayment : guildPaymentList) {
            GuiItem paymentItem = ItemBuilder.from(Material.SUNFLOWER)
                    .name(ComponentUtil.text("<light_purple>Wpłata " + guildPayment.getPayingName()))
                    .lore(ComponentUtil.asList(
                            "",
                            this.circle + " <gray>Ilość<dark_gray>: <light_purple>" + guildPayment.getAmountCoins(),
                            this.circle + " <gray>Data<dark_gray>: <light_purple>" + TimeUtil.formatDate(guildPayment.getPaymentDate().toInstant()),
                            "",
                            this.circle + " <gray>Saldo skarbca po wpłacie<dark_gray>: <light_purple>" + guildPayment.getBalanceAfterPayment(),
                            ""
                    ))
                    .glow(guildPayment.getAmountCoins() > 1000)
                    .asGuiItem();

            this.gui.addItem(paymentItem);
        }
    }
}

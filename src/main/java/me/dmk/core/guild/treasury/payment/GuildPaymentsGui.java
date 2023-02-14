package me.dmk.core.guild.treasury.payment;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.AllArgsConstructor;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.gui.item.storage.ItemStorage;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.guild.treasury.GuildTreasuryGui;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.StyleUtil;
import me.dmk.core.util.TimeUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;

/**
 * Created by DMK on 01.02.2023
 */

@AllArgsConstructor
public class GuildPaymentsGui {

    private final PluginConfiguration pluginConfiguration;
    private final LuckPermsController luckPermsController;
    private final ProfileController profileController;
    private final ProfileCache profileCache;
    private final GuildCache guildCache;

    public void open(Player player, Profile profile, Guild guild) {
        String circle = StyleUtil.getCircle();

        PaginatedGui gui = Gui.paginated()
                .title(ComponentUtil.text(circle + " <light_purple>Historia wpłat do skarbca " + circle))
                .rows(6)
                .disableAllInteractions()
                .create();

        GuiItem previousButton = ItemStorage.createPreviousPageButton(gui);
        GuiItem backButton = ItemStorage.createBackButton(event ->
                        new GuildTreasuryGui(this.pluginConfiguration,  this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile, guild),
                "",
                StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do menu skarbca gildyjnego<dark_gray>.",
                ""
        );
        GuiItem nextButton = ItemStorage.createNextPageButton(gui);

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
                            circle + " <gray>Ilość<dark_gray>: <light_purple>" + guildPayment.getAmountCoins(),
                            circle + " <gray>Data<dark_gray>: <light_purple>" + TimeUtil.format(guildPayment.getPaymentDate().toInstant()),
                            "",
                            circle + " <gray>Saldo skarbca po wpłacie<dark_gray>: <light_purple>" + guildPayment.getBalanceAfterPayment(),
                            ""
                    ))
                    .glow(guildPayment.getAmountCoins() > 1000)
                    .asGuiItem();

            gui.addItem(paymentItem);
        }

        gui.open(player);
    }
}

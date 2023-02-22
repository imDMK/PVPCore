package me.dmk.core.profile.punishment;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.gui.ProfilePanelGui;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.TimeUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;

/**
 * Created by DMK on 18.01.2023
 */

public class PunishmentHistoryGui extends PluginGui {

    public void open(Player player, Profile profile) {
        PaginatedGui gui = Gui.paginated()
                .title(ComponentUtil.text(this.circle + " <light_purple>Historia kar " + profile.getName() + " " + this.circle))
                .rows(6)
                .disableAllInteractions()
                .create();

        GuiItem previousButton = this.createPreviousPageButton(gui);
        GuiItem backButton = this.createBackButton(event ->
                        new ProfilePanelGui().open(player, profile),
                "",
                this.circle + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do panelu gracza<dark_gray>.",
                ""
        );
        GuiItem nextButton = this.createNextPageButton(gui);

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());

        gui.setItem(47, previousButton);
        gui.setItem(49, backButton);
        gui.setItem(51, nextButton);

        List<Punishment> punishmentList = profile.getPunishments()
                .stream()
                .sorted(Comparator.comparing(Punishment::getCreatedAt).reversed())
                .toList();

        int i = punishmentList.size();
        for (Punishment punishment : punishmentList) {

            boolean isBan = punishment.getType().equals(PunishmentType.BAN);
            boolean active = punishment.isActive();

            Material material;
            if (isBan) {
                material = Material.RED_GLAZED_TERRACOTTA;
            } else {
                material = Material.YELLOW_GLAZED_TERRACOTTA;
            }

            Component name = ComponentUtil.text("<light_purple>Kara #" + i);
            List<Component> lore = ComponentUtil.asList(
                    "",
                    this.circle + " <gray>Informacje o <light_purple>" + (punishment.isRemoved() ? "wycofanym" : active ? "aktywnym" : "wygaśniętym") + " " + (isBan ? "banie" : "wyciszeniu") + "<dark_gray>:",
                    "",
                    this.circle + " <gray>Administrator<dark_gray>: <light_purple>" + punishment.getAddedBy(),
                    this.circle + " <gray>Powód<dark_gray>: <light_purple>" + punishment.getReason(),
                    this.circle + " <gray>Wygasa<dark_gray>: <light_purple>" + (punishment.isPermanent() ? "nigdy" : active ? "za " + TimeUtil.instantToString(punishment.getExpireAt().toInstant(), true) : "wygasł"),
                    this.circle + " <gray>Data utworzenia<dark_gray>: <light_purple>" + TimeUtil.format(punishment.getCreatedAt().toInstant()),
                    ""
            );

            if (punishment.isRemoved()) {
                //lore.addAll(Arrays.asList(
                        //this.circle + " <gray>Wycofana przez<dark_gray>: <light_purple>" + punishment.getRemovedBy(),
                        //this.circle + " <gray>Data wycofania<dark_gray>: <light_purple>" + TimeUtil.format(punishment.getRemovedAt().toInstant()),
                        //""
                //));
            }

            GuiItem punishmentItem = ItemBuilder.from(material)
                    .name(name)
                    .lore(lore)
                    .glow(active)
                    .asGuiItem();

            gui.addItem(punishmentItem);
            i--;
        }

        gui.open(player);
    }
}

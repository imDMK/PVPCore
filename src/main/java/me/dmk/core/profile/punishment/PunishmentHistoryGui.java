package me.dmk.core.profile.punishment;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.RequiredArgsConstructor;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.gui.item.storage.ItemStorage;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.gui.ProfilePanelGui;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.StyleUtil;
import me.dmk.core.util.TimeUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;

/**
 * Created by DMK on 18.01.2023
 */

@RequiredArgsConstructor
public class PunishmentHistoryGui {

    private final PluginConfiguration pluginConfiguration;
    private final LuckPermsController luckPermsController;
    private final ProfileController profileController;
    private final ProfileCache profileCache;
    private final GuildCache guildCache;

    public void open(Player player, Profile profile) {
        String circle = StyleUtil.getCircle();

        PaginatedGui gui = Gui.paginated()
                .title(ComponentUtil.text(circle + " <light_purple>Historia kar " + profile.getName() + " " + circle))
                .rows(6)
                .disableAllInteractions()
                .create();

        GuiItem previousButton = ItemStorage.createPreviousPageButton(gui);
        GuiItem backButton = ItemStorage.createBackButton(event ->
                        new ProfilePanelGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile),
                "",
                circle + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do panelu gracza<dark_gray>.",
                ""
        );
        GuiItem nextButton = ItemStorage.createNextPageButton(gui);

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
                    circle + " <gray>Informacje o <light_purple>" + (punishment.isRemoved() ? "wycofanym" : active ? "aktywnym" : "wygaśniętym") + " " + (isBan ? "banie" : "wyciszeniu") + "<dark_gray>:",
                    "",
                    circle + " <gray>Administrator<dark_gray>: <light_purple>" + punishment.getAddedBy(),
                    circle + " <gray>Powód<dark_gray>: <light_purple>" + punishment.getReason(),
                    circle + " <gray>Wygasa<dark_gray>: <light_purple>" + (punishment.isPermanent() ? "nigdy" : active ? "za " + TimeUtil.instantToString(punishment.getExpireAt().toInstant(), true) : "wygasł"),
                    circle + " <gray>Data utworzenia<dark_gray>: <light_purple>" + TimeUtil.format(punishment.getCreatedAt().toInstant()),
                    ""
            );

            if (punishment.isRemoved()) {
                lore.addAll(ComponentUtil.asList(
                        circle + " <gray>Wycofana przez<dark_gray>: <light_purple>" + punishment.getRemovedBy(),
                        circle + " <gray>Data wycofania<dark_gray>: <light_purple>" + TimeUtil.format(punishment.getRemovedAt().toInstant()),
                        ""
                ));
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

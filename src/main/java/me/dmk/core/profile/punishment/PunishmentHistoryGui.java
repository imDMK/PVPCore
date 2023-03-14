package me.dmk.core.profile.punishment;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginPaginatedGui;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.gui.ProfilePanelGui;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.TimeUtil;
import me.dmk.core.util.string.StringFormatter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by DMK on 02.03.2023
 */

public class PunishmentHistoryGui extends PluginPaginatedGui {

    private final Profile profile;

    private final PunishmentType punishmentType;

    public PunishmentHistoryGui(Player player, Profile profile, PunishmentType punishmentType) {
        super(player, "Historia kar " + profile.getName(), 6, true, true);

        this.profile = profile;
        this.punishmentType = punishmentType;
    }

    @Override
    public void build() {
        boolean isSelf = this.player.getUniqueId().equals(this.profile.getUuid());

        GuiItem previousButton = this.createPreviousPageButton(this.gui);
        GuiItem backButton = this.createBackButton(event ->
                        new ProfilePanelGui(this.player, this.profile).open(),
                "",
                this.circle + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do panelu gracza<dark_gray>.",
                ""
        );
        GuiItem nextButton = this.createNextPageButton(this.gui);

        GuiItem sortItem = ItemBuilder.from(
                (this.punishmentType == null ? Material.REDSTONE_BLOCK :
                        (this.punishmentType == PunishmentType.BAN ? Material.RED_GLAZED_TERRACOTTA : Material.YELLOW_GLAZED_TERRACOTTA)
                ))
                .name(ComponentUtil.text("<gray>Sortowanie<dark_gray>: <light_purple>" +
                        (this.punishmentType == null ? "Wszystkie" : (this.punishmentType == PunishmentType.BAN ? "Bany" : "Wyciszenia"))
                ))
                .lore(ComponentUtil.asList(
                        "",
                        StringFormatter.formatWarning() + " <gold>Kliknij<dark_gray>, <gray>aby zmienić sortowanie<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    if (this.punishmentType == null) {
                        new PunishmentHistoryGui(this.player, this.profile, PunishmentType.BAN).open();
                    } else if (this.punishmentType == PunishmentType.BAN) {
                        new PunishmentHistoryGui(this.player, this.profile, PunishmentType.MUTE).open();
                    } else {
                        new PunishmentHistoryGui(this.player, this.profile, null).open();
                    }
                });

        this.gui.setItem(47, previousButton);
        this.gui.setItem(48, sortItem);
        this.gui.setItem(49, backButton);
        this.gui.setItem(50, sortItem);
        this.gui.setItem(51, nextButton);

        List<Punishment> punishments = this.profile.getPunishments()
                .stream()
                .filter(p -> {
                    if (this.punishmentType == null) {
                        return true;
                    }

                    return p.getType().equals(this.punishmentType);
                })
                .sorted(Comparator.comparing(Punishment::getCreatedAt).reversed())
                .toList();

        int i = punishments.size();
        for (Punishment punishment : punishments) {
            boolean isBan = punishment.getType().equals(PunishmentType.BAN);

            Material material = (isBan ? Material.RED_GLAZED_TERRACOTTA : Material.YELLOW_GLAZED_TERRACOTTA);

            Component name = ComponentUtil.text("<light_purple>Kara #" + i);
            List<String> lore = new ArrayList<>(Arrays.asList(
                    "",
                    this.circle + " <gray>Informacje o <light_purple>" + (punishment.isRemoved() ? "wycofanym" : punishment.isActive() ? "aktywnym" : "wygaśniętym") + " " + (isBan ? "banie" : "wyciszeniu") + "<dark_gray>:",
                    "",
                    this.circle + " <gray>Administrator<dark_gray>: <light_purple>" + punishment.getAddedBy(),
                    ""
            ));

            if (isSelf || this.player.hasPermission("core.punishment.see.details")) {
                lore.addAll(Arrays.asList(
                        this.circle + " <gray>Powód<dark_gray>: <light_purple>" + punishment.getReason(),
                        this.circle + " <gray>Wygasa<dark_gray>: <light_purple>" + (punishment.isPermanent() ? "nigdy" : punishment.isActive() ? "za " + TimeUtil.instantToString(punishment.getExpireAt().toInstant(), true) : "wygasł"),
                        this.circle + " <gray>Data utworzenia<dark_gray>: <light_purple>" + TimeUtil.formatDate(punishment.getCreatedAt().toInstant()),
                        ""
                ));

                if (punishment.isRemoved()) {
                    lore.addAll(Arrays.asList(
                            this.circle + " <gray>Wycofana przez<dark_gray>: <light_purple>" + punishment.getRemovedBy(),
                            this.circle + " <gray>Data wycofania<dark_gray>: <light_purple>" + TimeUtil.formatDate(punishment.getRemovedAt().toInstant()),
                            ""
                    ));
                }
            }

            GuiItem punishmentItem = ItemBuilder.from(material)
                    .name(name)
                    .lore(ComponentUtil.asList(lore))
                    .glow(punishment.isActive())
                    .asGuiItem();

            this.gui.addItem(punishmentItem);
            i--;
        }
    }
}

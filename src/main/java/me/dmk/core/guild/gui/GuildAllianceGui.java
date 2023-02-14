package me.dmk.core.guild.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.AllArgsConstructor;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.gui.ConfirmationGui;
import me.dmk.core.gui.item.storage.ItemStorage;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.StyleUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by DMK on 01.02.2023
 */

@AllArgsConstructor
public class GuildAllianceGui {

    private final PluginConfiguration pluginConfiguration;
    private final LuckPermsController luckPermsController;
    private final ProfileController profileController;
    private final ProfileCache profileCache;
    private final GuildCache guildCache;

    public void open(Player player, Profile profile, Guild guild) {
        String circle = StyleUtil.getCircle();

        PaginatedGui gui = Gui.paginated()
                .title(ComponentUtil.text(circle + "<light_purple>Lista członków " + circle))
                .rows(6)
                .disableAllInteractions()
                .create();

        boolean isLeaderOrCoLeader = guild.isLeaderOrCoLeader(player.getUniqueId());

        GuiItem previousButton = ItemStorage.createPreviousPageButton(gui);
        GuiItem backButton = ItemStorage.createBackButton(event ->
                        new GuildPanelGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile, guild),
                "",
                StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do panelu gildii<dark_gray>.",
                ""
        );
        GuiItem nextButton = ItemStorage.createNextPageButton(gui);

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());

        gui.setItem(47, previousButton);
        gui.setItem(49, backButton);
        gui.setItem(51, nextButton);

        for (String guildTag : guild.getAlliances()) {
            Optional<Guild> allianceGuildOptional = this.guildCache.getOrElseLoad(guildTag);
            if (allianceGuildOptional.isEmpty()) {
                guild.getAlliances().remove(guildTag);
                continue;
            }

            Guild allianceGuild = allianceGuildOptional.get();

            List<Component> lore = ComponentUtil.asList(
                    "",
                    circle + " <gray>Nazwa<dark_gray>: <light_purple>" + allianceGuild.getName(),
                    ""
            );

            if (isLeaderOrCoLeader) {
                lore.addAll(List.of(
                    ComponentUtil.text(StyleUtil.getWarning() + " <light_purple>Kliknij LPM<dark_gray>, <gray>aby przejść do panelu tej gildii<dark_gray>."),
                    ComponentUtil.text(StyleUtil.getWarning() + " <light_purple>Kliknij SHIFT + PPM<dark_gray>, <gray>aby <red>zerwać sojusz<dark_gray>.")
                ));
            }

            GuiItem allianceGuildItem = ItemBuilder.from(Material.BEACON)
                    .name(ComponentUtil.text(StyleUtil.getPurpleGradient() + allianceGuild.getTag()))
                    .lore(lore)
                    .asGuiItem(event -> {
                        if (event.isLeftClick()) {
                            new GuildPanelGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile, allianceGuild);
                        } else if (event.isRightClick() && event.isShiftClick()) {
                            new ConfirmationGui(player)
                                    .create(circle + " <light_purple>Zerwanie sojuszu z " + allianceGuild.getTag() + " " + circle)
                                    .afterConfirm(e -> {
                                        Bukkit.dispatchCommand(player, "guild alliance break " + allianceGuild.getTag());
                                        this.open(player, profile, guild);
                                    })
                                    .afterCancel(e -> this.open(player, profile, guild))
                                    .open();
                        }
                    });

            gui.addItem(allianceGuildItem);
        }

        gui.open(player);
    }
}

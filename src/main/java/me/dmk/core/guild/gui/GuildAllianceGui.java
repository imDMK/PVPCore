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
import me.dmk.core.util.string.StringUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

/**
 * Created by DMK on 01.02.2023
 */

@AllArgsConstructor
public class GuildAllianceGui extends ItemStorage {

    private final PluginConfiguration pluginConfiguration;
    private final LuckPermsController luckPermsController;
    private final ProfileController profileController;
    private final ProfileCache profileCache;
    private final GuildCache guildCache;

    public void open(Player player, Profile profile, Guild guild) {
        PaginatedGui gui = Gui.paginated()
                .title(ComponentUtil.text(this.circle + "<light_purple>Lista członków " + this.circle))
                .rows(6)
                .disableAllInteractions()
                .create();

        boolean isLeaderOrCoLeader = guild.isLeaderOrCoLeader(player.getUniqueId());

        GuiItem previousButton = this.createPreviousPageButton(gui);
        GuiItem backButton = this.createBackButton(event ->
                        new GuildPanelGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile, guild),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do panelu gildii<dark_gray>.",
                ""
        );
        GuiItem nextButton = this.createNextPageButton(gui);

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
                    this.circle + " <gray>Nazwa<dark_gray>: <light_purple>" + allianceGuild.getName(),
                    ""
            );

            if (isLeaderOrCoLeader) {
                lore.addAll(List.of(
                    ComponentUtil.text(this.warning + " <light_purple>Kliknij LPM<dark_gray>, <gray>aby przejść do panelu tej gildii<dark_gray>."),
                    ComponentUtil.text(this.warning + " <light_purple>Kliknij SHIFT + PPM<dark_gray>, <gray>aby <red>zerwać sojusz<dark_gray>.")
                ));
            }

            GuiItem allianceGuildItem = ItemBuilder.from(Material.BEACON)
                    .name(ComponentUtil.text(StringUtil.getPurpleGradient() + allianceGuild.getTag()))
                    .lore(lore)
                    .asGuiItem(event -> {
                        if (event.isLeftClick()) {
                            new GuildPanelGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile, allianceGuild);
                        } else if (event.isRightClick() && event.isShiftClick()) {
                            new ConfirmationGui(player)
                                    .create(this.circle + " <light_purple>Zerwanie sojuszu z " + allianceGuild.getTag() + " " + this.circle)
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

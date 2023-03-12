package me.dmk.core.guild.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.CorePlugin;
import me.dmk.core.gui.PluginPaginatedGui;
import me.dmk.core.gui.confirmation.ConfirmationGui;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.profile.Profile;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by DMK on 02.03.2023
 */

public class GuildAllianceListGui extends PluginPaginatedGui {

    private final GuildCache guildCache = CorePlugin.getCorePlugin().getGuildCache();

    private final Profile profile;
    private final Guild guild;

    public GuildAllianceListGui(Player player, Profile profile, Guild guild) {
        super(player, "Lista sojuszy", 6, true, true);

        this.profile = profile;
        this.guild = guild;
    }

    @Override
    public void build() {
        GuiItem previousButton = this.createPreviousPageButton(this.gui);
        GuiItem backButton = this.createBackButton(event ->
                        new GuildPanelGui(this.player, this.profile, this.guild).open(),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do panelu gildii<dark_gray>.",
                ""
        );
        GuiItem nextButton = this.createNextPageButton(this.gui);

        this.gui.setItem(47, previousButton);
        this.gui.setItem(49, backButton);
        this.gui.setItem(51, nextButton);

        boolean canManageAlliances = this.guild.getGuildRank(this.player.getUniqueId()).isCanManageAlliances();

        for (String guildTag : this.guild.getAlliances()) {
            Optional<Guild> allianceGuildOptional = this.guildCache.getOrElseLoad(guildTag);
            if (allianceGuildOptional.isEmpty()) {
                this.guild.getAlliances().remove(guildTag);
                continue;
            }

            Guild allianceGuild = allianceGuildOptional.get();

            List<String> lore = new ArrayList<>(Arrays.asList(
                    "",
                    this.circle + " <gray>Nazwa<dark_gray>: <light_purple>" + allianceGuild.getName(),
                    ""
            ));

            if (canManageAlliances) {
                lore.addAll(Arrays.asList(
                        this.warning + " <light_purple>Kliknij LPM<dark_gray>, <gray>aby przejść do panelu tej gildii<dark_gray>.",
                        this.warning + " <light_purple>Kliknij SHIFT + PPM<dark_gray>, <gray>aby <red>zerwać sojusz<dark_gray>."
                ));
            }

            GuiItem allianceGuildItem = ItemBuilder.from(Material.BEACON)
                    .name(ComponentUtil.text(StringFormatter.formatPurpleGradient() + allianceGuild.getTag()))
                    .lore(ComponentUtil.asList(lore))
                    .asGuiItem(event -> {
                        if (event.isLeftClick()) {
                            new GuildPanelGui(this.player, this.profile, allianceGuild).open();

                        } else if (event.isRightClick() && event.isShiftClick()) {
                            new ConfirmationGui(this.player)
                                    .title("Potwierdź zerwanie sojuszu z " + allianceGuild.getTag())
                                    .afterConfirm(e -> {
                                        Bukkit.dispatchCommand(player, "guild alliance break " + allianceGuild.getTag());
                                        this.open();
                                    })
                                    .afterCancel(e -> this.open())
                                    .open();
                        }
                    });

            this.gui.addItem(allianceGuildItem);
        }
    }
}

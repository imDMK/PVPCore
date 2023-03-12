package me.dmk.core.guild.rank.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.gui.GuildPanelGui;
import me.dmk.core.guild.rank.GuildRank;
import me.dmk.core.profile.Profile;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by DMK on 11.03.2023
 */

public class GuildRanksGui extends PluginGui {

    private final Guild guild;

    public GuildRanksGui(Player player, Profile profile, Guild guild) {
        super(player, profile, "Rangi gildyjne", 6, true, true);

        this.guild = guild;
    }

    @Override
    public void build() {
        GuiItem backButton = this.createBackButton(event ->
                        new GuildPanelGui(this.player, this.profile, this.guild).open(),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do panelu gildii<dark_gray>.",
                ""
        );

        this.gui.setItem(49, backButton);

        Collection<GuildRank> guildMemberList = this.guild.getGuildRanks().values()
                .stream()
                .sorted(Comparator.comparingInt(GuildRank::getPriority))
                .toList();

        boolean canManageRanks = this.guild.isLeader(this.player.getUniqueId()) || this.guild.getGuildRank(this.player.getUniqueId()).isCanManageRanks();

        for (GuildRank guildRank : guildMemberList) {
            List<String> lore = new ArrayList<>(Arrays.asList(
                    "",
                    this.circle + " <gray>Priorytet<dark_gray>: <light_purple>" + guildRank.getPriority(),
                    this.circle + " <gray>Może zarządzać członkami<dark_gray>: " + StringFormatter.formatBooleanYesOrNo(guildRank.isCanManageMembers()),
                    this.circle + " <gray>Może zarządzać sojuszami<dark_gray>: " + StringFormatter.formatBooleanYesOrNo(guildRank.isCanManageAlliances()),
                    this.circle + " <gray>Może zarządzać rangami<dark_gray>: " + StringFormatter.formatBooleanYesOrNo(guildRank.isCanManageRanks()),
                    this.circle + " <gray>Może przedłużyć gildię<dark_gray>: " + StringFormatter.formatBooleanYesOrNo(guildRank.isCanExtend()),
                    ""
            ));

            if (canManageRanks) {
                lore.addAll(Arrays.asList(
                        StringFormatter.formatWarning() + " <gold>Kliknij<dark_gray>, <gray>aby <gold>edytować rangę<dark_gray>.",
                        ""
                ));
            }

            GuiItem guildRankItem = ItemBuilder.from(guildRank.getIcon())
                    .name(ComponentUtil.text(guildRank.getName()))
                    .lore(ComponentUtil.asList(lore))
                    .asGuiItem(event -> {
                        if (!canManageRanks) {
                            return;
                        }

                        new GuildRankEditGui(this.player, this.profile, this.guild, guildRank).open();
                    });

            this.gui.addItem(guildRankItem);
        }
    }
}

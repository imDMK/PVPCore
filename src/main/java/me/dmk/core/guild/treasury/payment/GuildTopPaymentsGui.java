package me.dmk.core.guild.treasury.payment;

import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.gui.item.storage.SkullStorage;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.member.GuildMember;
import me.dmk.core.guild.treasury.GuildTreasuryGui;
import me.dmk.core.profile.Profile;
import me.dmk.core.util.ComponentUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;

/**
 * Created by DMK on 02.03.2023
 */

public class GuildTopPaymentsGui extends PluginGui {

    public final Guild guild;

    public GuildTopPaymentsGui(Player player, Profile profile, Guild guild) {
        super(player, profile, "Topka wpłaconych monet", 6, true, true);

        this.guild = guild;
    }

    @Override
    public void build() {
        GuiItem backButton = this.createBackButton(event ->
                        new GuildTreasuryGui(this.player, this.profile, this.guild).open(),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do menu skarbca gildii<dark_gray>.",
                ""
        );

        this.gui.setItem(40, backButton);

        List<GuildMember> guildMembers = this.guild.getMembers().values()
                .stream()
                .sorted(Comparator.comparingInt(GuildMember::getAddedCoinsToTreasury))
                .limit(10)
                .toList();

        for (int i = 0; i < guildMembers.size(); i++) {
            GuildMember guildMember = guildMembers.get(i);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(guildMember.getUuid());

            GuiItem item = SkullStorage.createPlayerHead(offlinePlayer.getUniqueId())
                    .name(ComponentUtil.text((i + 1) + " <light_purple>" + offlinePlayer.getName()))
                    .lore(ComponentUtil.asList(
                            "",
                            this.circle + " <gray>Członek <light_purple>" + offlinePlayer.getName() + " <gray>wpłacił <light_purple>" + guildMember.getAddedCoinsToTreasury() + " <gray>monet<dark_gray>.",
                            ""
                    ))
                    .glow(i == 0)
                    .asGuiItem();

            this.gui.addItem(item);
        }
    }
}

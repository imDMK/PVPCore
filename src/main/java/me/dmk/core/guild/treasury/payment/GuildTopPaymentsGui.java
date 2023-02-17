package me.dmk.core.guild.treasury.payment;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.AllArgsConstructor;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.gui.item.storage.ItemStorage;
import me.dmk.core.gui.item.storage.SkullStorage;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.guild.member.GuildMember;
import me.dmk.core.guild.treasury.GuildTreasuryGui;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.util.ComponentUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;

/**
 * Created by DMK on 01.02.2023
 */

@AllArgsConstructor
public class GuildTopPaymentsGui extends ItemStorage {

    private final PluginConfiguration pluginConfiguration;
    private final LuckPermsController luckPermsController;
    private final ProfileController profileController;
    private final ProfileCache profileCache;
    private final GuildCache guildCache;

    public void open(Player player, Profile profile, Guild guild) {
        Gui gui = Gui.gui()
                .title(ComponentUtil.text("<light_purple>Topka wpłaconych monet"))
                .rows(6)
                .disableAllInteractions()
                .create();

        GuiItem backButton = this.createBackButton(event ->
                        new GuildTreasuryGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile, guild),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do menu skarbca gildii<dark_gray>.",
                ""
        );

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());
        gui.setItem(40, backButton);

        List<GuildMember> guildMembers = guild.getMembers().values()
                .stream()
                .sorted(Comparator.comparingInt(GuildMember::getAddedCoinsToTreasury))
                .limit(10)
                .toList();

        for (int i = 0; i < guildMembers.size(); i++) {
            GuildMember guildMember = guildMembers.get(i);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(guildMember.getUuid());

            GuiItem item = SkullStorage.createPlayerHead(offlinePlayer)
                    .name(ComponentUtil.text((i + 1) + "<light_purple>" + offlinePlayer.getName()))
                    .lore(ComponentUtil.asList(
                            "",
                            this.circle + " <gray>Członek <light_purple>" + offlinePlayer.getName() + " <gray>wpłacił <light_purple>" + guildMember.getAddedCoinsToTreasury() + " <gray>monet<dark_gray>.",
                            ""
                    ))
                    .glow(i == 0)
                    .asGuiItem();

            gui.addItem(item);
        }

        gui.open(player);
    }
}

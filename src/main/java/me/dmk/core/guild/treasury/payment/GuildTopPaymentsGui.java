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
import me.dmk.core.guild.member.Member;
import me.dmk.core.guild.treasury.GuildTreasuryGui;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.StyleUtil;
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
public class GuildTopPaymentsGui {

    private final PluginConfiguration pluginConfiguration;
    private final LuckPermsController luckPermsController;
    private final ProfileController profileController;
    private final ProfileCache profileCache;
    private final GuildCache guildCache;

    public void open(Player player, Profile profile, Guild guild) {
        String circle = StyleUtil.getCircle();

        Gui gui = Gui.gui()
                .title(ComponentUtil.text("<light_purple>Topka wpłaconych monet"))
                .rows(6)
                .disableAllInteractions()
                .create();

        GuiItem backButton = ItemStorage.createBackButton(event ->
                        new GuildTreasuryGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache).open(player, profile, guild),
                "",
                StyleUtil.getWarning() + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do menu skarbca gildii<dark_gray>.",
                ""
        );

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());
        gui.setItem(40, backButton);

        List<Member> members = guild.getMembers().values()
                .stream()
                .sorted(Comparator.comparingInt(Member::getAddedCoinsToTreasury))
                .limit(10)
                .toList();

        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(member.getUuid());

            GuiItem item = SkullStorage.createPlayerHead(offlinePlayer)
                    .name(ComponentUtil.text((i + 1) + "<light_purple>" + offlinePlayer.getName()))
                    .lore(ComponentUtil.asList(
                            "",
                            circle + " <gray>Członek <light_purple>" + offlinePlayer.getName() + " <gray>wpłacił <light_purple>" + member.getAddedCoinsToTreasury() + " <gray>monet<dark_gray>.",
                            ""
                    ))
                    .glow(i == 0)
                    .asGuiItem();

            gui.addItem(item);
        }

        gui.open(player);
    }
}

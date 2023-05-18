package me.dmk.core.profile.settings.board;

import fr.mrmicky.fastboard.adventure.FastBoard;
import lombok.Data;
import me.dmk.core.CorePlugin;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.guild.Guild;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.statistics.ProfileStatistics;
import me.dmk.core.util.PlayerUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DMK on 05.01.2023
 */

@Data
public class Board implements Serializable {

    private boolean enabled = true;
    private transient FastBoard fastBoard;

    public void create(Player player, Profile profile) {
        this.fastBoard = new FastBoard(player);
        this.update(player, profile);
    }

    public void update(Player player, Profile profile) {
        PluginConfiguration pluginConfiguration = CorePlugin.getCorePlugin().getPluginConfiguration();
        LuckPermsController luckPermsController = CorePlugin.getCorePlugin().getLuckPermsController();
        MiniMessage miniMessage = CorePlugin.getCorePlugin().getMiniMessage();

        ProfileStatistics statistics = profile.getProfileStatistics();

        String group = luckPermsController.getHighestGroupDisplayNameOrName(player.getUniqueId()).orElse("Brak");
        String guild = profile.getGuild().map(Guild::getTag).orElse("Brak");

        int coins = statistics.getCoins();
        int ping = player.getPing();
        int points = statistics.getPoints();
        int kills = statistics.getKills();
        int deaths = statistics.getDeaths();
        int killStreak = statistics.getKillStreak();
        double kdr = PlayerUtil.getKDR(kills, deaths);

        TagResolver tagResolver = TagResolver.resolver(
                Placeholder.unparsed("group", group),
                Placeholder.unparsed("coins", String.valueOf(coins)),
                Placeholder.unparsed("ping", String.valueOf(ping)),
                Placeholder.unparsed("guild", guild),
                Placeholder.unparsed("points", String.valueOf(points)),
                Placeholder.unparsed("kills", String.valueOf(kills)),
                Placeholder.unparsed("deaths", String.valueOf(deaths)),
                Placeholder.unparsed("killstreak", String.valueOf(killStreak)),
                Placeholder.unparsed("kdr", String.valueOf(kdr))
        );

        List<Component> boardLines = new ArrayList<>();

        pluginConfiguration.getSidebarLines().forEach(string ->
                boardLines.add(
                        miniMessage.deserialize(string, tagResolver)
                )
        );

        this.fastBoard.updateLines(boardLines);
        this.fastBoard.updateTitle(
                miniMessage.deserialize(pluginConfiguration.getSidebarName())
        );
    }

    public void remove() {
        if (this.fastBoard != null && !this.fastBoard.isDeleted()) {
            this.fastBoard.delete();
        }
    }
}

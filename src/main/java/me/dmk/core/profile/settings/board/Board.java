package me.dmk.core.profile.settings.board;

import fr.mrmicky.fastboard.FastBoard;
import lombok.Data;
import me.dmk.core.CorePlugin;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.guild.Guild;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.statistics.ProfileStatistics;
import me.dmk.core.util.PlayerUtil;
import me.dmk.core.util.string.StringUtil;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        ProfileStatistics statistics = profile.getProfileStatistics();

        Optional<String> group = luckPermsController.getHighestGroupDisplayNameOrName(player.getUniqueId());
        Optional<Guild> guild = profile.getGuild();

        String coins = String.valueOf(statistics.getCoins());
        String ping = String.valueOf(player.getPing());
        String points = String.valueOf(statistics.getPoints());
        String kills = String.valueOf(statistics.getKills());
        String deaths = String.valueOf(statistics.getDeaths());
        String killStreak = String.valueOf(statistics.getKills());
        String kdr = String.valueOf(PlayerUtil.getKDR(statistics.getKills(), statistics.getDeaths()));

        List<String> boardList = new ArrayList<>();

        pluginConfiguration.getSidebarList().forEach(string ->
                boardList.add(
                        StringUtil.colorLegacy(string
                                .replace("<rank>", group.orElse("Brak"))
                                .replace("<coins>", coins)
                                .replace("<ping>", ping)
                                .replace("<guild>", guild.map(Guild::getTag).orElse("Brak"))
                                .replace("<points>", points)
                                .replace("<kills>", kills)
                                .replace("<deaths>", deaths)
                                .replace("<killstreak>", killStreak)
                                .replace("<kdr>", kdr)
                        )
                )
        );

        this.fastBoard.updateLines(boardList);
        this.fastBoard.updateTitle(StringUtil.colorLegacy(pluginConfiguration.getSidebarName()));
    }

    public void remove() {
        if (this.fastBoard != null && !this.fastBoard.isDeleted()) {
            this.fastBoard.delete();
        }
    }
}

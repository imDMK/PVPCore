package me.dmk.core.util;

import lombok.experimental.UtilityClass;
import me.dmk.core.murder.MurderType;
import me.dmk.core.profile.statistics.ProfileStatistics;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 14.02.2023
 */

@UtilityClass
public class PlayerUtil {

    public static MurderType getMurderType(Player victim, ProfileStatistics victimStatistics, Player killer, boolean revenge) {
        double distance = victim.getLocation().distance(killer.getLocation());

        if (distance > 20) {
            return MurderType.WHILE_VICTIM_RUNNING;

        } else if (System.currentTimeMillis() - victimStatistics.getLastEatTime() < 1600) {
            return MurderType.WHILE_VICTIM_EATING;

        } else if (revenge) {
            return MurderType.REVENGE;
        }

        return MurderType.DEFAULT;
    }

    public static double getKDR(double kills, double deaths) {
        if (kills == 0 && deaths == 0) {
            return 0.0;
        }

        if (kills > 0 && deaths == 0) {
            return kills;
        }

        if (deaths > 0 && kills == 0) {
            return -deaths;
        }

        double pow = Math.pow(10, 2);
        return Math.round(kills / deaths * pow) / pow;
    }

    public static int getRankChange(int victimPoints, int killerPoints) {
        int change = victimPoints - killerPoints;

        if (change < 500) {
            return 20;
        }

        double divider = 10.0D;

        double pow = 1.0D / (1.0D + Math.pow(divider, change));
        return (int) pow;
    }

    public static int getSecondsPlayed(Player player) {
        return player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20; //20 ticks = 1 second
    }
}

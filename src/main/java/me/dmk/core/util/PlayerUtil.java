package me.dmk.core.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 14.02.2023
 */

@UtilityClass
public class PlayerUtil {

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

    public static int getSecondsPlayed(Player player) {
        return player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20; //20 ticks = 1 second
    }
}

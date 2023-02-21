package me.dmk.core.util;

import lombok.experimental.UtilityClass;
import me.dmk.core.murder.MurderType;
import me.dmk.core.profile.statistics.ProfileStatistics;
import me.dmk.core.util.string.StringUtil;
import me.dmk.core.util.string.SymbolUtil;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 18.02.2023
 */

@UtilityClass
public class MurderUtil {

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

    public static String formatMurderNotification(MurderType murderType) {
        return switch (murderType) {
            case DEFAULT -> "Umarłeś(-aś)!";
            case WHILE_VICTIM_EATING -> "Polizałeś(-aś)!";
            case WHILE_VICTIM_RUNNING -> "Umarłeś(-aś) podczas ucieczki!";
            case REVENGE -> "Zemsta";
        };
    }

    public static String formatMurderType(MurderType murderType) {
        return switch (murderType) {
            case DEFAULT -> "zabity(-a) przez";
            case WHILE_VICTIM_EATING -> "polizał(-a) przez";
            case WHILE_VICTIM_RUNNING -> "próbował(-a) uciec przed";
            case REVENGE -> "pożałował(-a) zabijając";
        };
    }

    public static String formatDeathMessage(String victim, int removedPoints, MurderType murderType, String killer, int addedPoints) {
        return StringUtil.getOpeningSquareBracket() + SymbolUtil.getDeath("<red>") + StringUtil.getClosingSquareBracket() +
                " <red>Gracz " + victim + " "  + StringUtil.getOpeningSquareBracket() + "<red>-" + removedPoints + StringUtil.getClosingSquareBracket() +
                " <red>" + formatMurderType(murderType) + " " + killer + " " + StringUtil.getOpeningSquareBracket() + "<green>+" + addedPoints + StringUtil.getClosingSquareBracket() +
                "<dark_gray>.";
    }

    public static int calulcateAddPoints(MurderType murderType, int victimPoints, int killerPoints) {
        int change = (victimPoints - killerPoints) / 3;

        int add = (int) (murderType.getPointsMultiplier() * change);
        if (add < 25) {
            add = 25;
        }

        return Math.min(add, 200);
    }

    public static int calculateRemovePoints(MurderType murderType, int addedPoints) {
        int remove = (int) (2 * (addedPoints / 4) * murderType.getPointsMultiplier());
        if (remove < 25) {
            remove = 25;
        }

        return Math.min(remove, 150);
    }
}

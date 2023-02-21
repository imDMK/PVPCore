package me.dmk.core.util.string;

import lombok.experimental.UtilityClass;

/**
 * Created by DMK on 18.02.2023
 */

@UtilityClass
public class SymbolUtil {

    public static String getSmile(String color) {
        return color + "☻";
    }

    public static String getStar(String color) {
        return color + "✪";
    }

    public static String getStarSecond(String color) {
        return color + "✦";
    }

    public static String getSword(String color) {
        return color + "\uD83D\uDDE1";
    }

    public static String getHeart(String color) {
        return color + "❤";
    }

    public static String getEnvelope(String color) {
        return color + "✉";
    }

    public static String getWatch(String color) {
        return color + "⌚";
    }

    public static String getCheckMark(String color) {
        return color + "<b>✔</b>";
    }

    public static String getExclamationMark(String color) {
        return color + "<b>!</b>";
    }

    public static String getCrossMark(String color) {
        return color + "<b>✘</b>";
    }

    public static String getCircle(String color) {
        return color + "●";
    }

    public static String getDeath(String color) {
        return color + "☠";
    }
}

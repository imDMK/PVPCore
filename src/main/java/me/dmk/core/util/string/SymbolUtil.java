package me.dmk.core.util.string;

import lombok.experimental.UtilityClass;

/**
 * Created by DMK on 18.02.2023
 */

@UtilityClass
public class SymbolUtil {

    public static String getSmile() {
        return "☻";
    }

    public static String getStar() {
        return "✪";
    }

    public static String getStarSecond() {
        return "✦";
    }

    public static String getSword() {
        return "\uD83D\uDDE1";
    }

    public static String getHeart() {
        return "❤";
    }

    public static String getEnvelope() {
        return "✉";
    }

    public static String getWatch() {
        return "⌚";
    }

    public static String getCheckMark() {
        return "<b>✔</b>";
    }

    public static String getExclamationMark() {
        return "<b>!</b>";
    }

    public static String getCrossMark() {
        return "<b>✘</b>";
    }

    public static String getCircle(String color) {
        return color + "●";
    }

    public static String getDeath() {
        return "☠";
    }
}

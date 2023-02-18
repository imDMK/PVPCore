package me.dmk.core.util.string;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.ChatColor;

/**
 * Created by DMK on 18.02.2023
 */

@UtilityClass
public class StringUtil {

    public static String colorLegacy(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String getOpeningSquareBracket() {
        return "<dark_gray>[</dark_gray>";
    }

    public static String getClosingSquareBracket() {
        return "<dark_gray>]</dark_gray>";
    }

    public static String getPurpleGradient() {
        return "<gradient:light_purple:dark_purple>";
    }

    public static String getGreenGradient() {
        return "<gradient:green:dark_green>";
    }

    public static String getRedGradient() {
        return "<gradient:red:dark_red>";
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    public static String generateRandomString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }
}

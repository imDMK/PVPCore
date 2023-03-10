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

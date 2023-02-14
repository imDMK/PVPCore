package me.dmk.core.util;

import lombok.experimental.UtilityClass;
import me.dmk.core.guild.Guild;
import me.dmk.core.murder.MurderType;
import me.dmk.core.profile.punishment.Punishment;
import me.dmk.core.profile.punishment.PunishmentType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Created by DMK on 30.12.2022
 */

@UtilityClass
public class StyleUtil {

    /* Legacy colored formating */
    public static String colored(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /* MiniMessage formatting */
    public static String getSquareBracketStart() {
        return "<dark_gray>[</dark_gray>";
    }

    public static String getSquareBracketEnd() {
        return "<dark_gray>]</dark_gray>";
    }

    public static String getSilent() {
        return getSquareBracketStart() + "<dark_gray>S" + getSquareBracketEnd();
    }

    public static String getSmile() {
        return "☻";
    }

    public static String getSuccess() {
        return getSquareBracketStart() + "<green><b>✔</b>" + getSquareBracketEnd();
    }

    public static String getWarning() {
        return getSquareBracketStart() + "<gold><b>!</b>" + getSquareBracketEnd();
    }

    public static String getError() {
        return getSquareBracketStart() + "<dark_red><b>✘</b>" + getSquareBracketEnd();
    }

    public static String getCircle() {
        return "<dark_gray>●";
    }

    public static String getDeath() {
        return getSquareBracketStart() + "<dark_red>☠" + getSquareBracketEnd();
    }

    public static String getGuild() {
        return getSquareBracketStart() + getGreenGradient() + "GILDIA" + getSquareBracketEnd();
    }

    /* Gradients */
    public static String getPurpleGradient() {
        return "<gradient:light_purple:dark_purple>";
    }
    public static String getGreenGradient() {
        return "<gradient:green:dark_green>";
    }

    public static String getRedGradient() {
        return "<gradient:red:dark_red>";
    }

    /* Formatters */
    public static String formatBoolean(boolean status) {
        return (status ? getGreenGradient() + "włączony" : getRedGradient() + "wyłączony") + "</gradient>";
    }

    public static String formatBoolean(boolean status, char symbol) {
        return (status ? getGreenGradient() + "włączon" : getRedGradient() + "wyłączon") + symbol + "</gradient>";
    }

    public static String formatIncognito(String incognitoIdentifier) {
        return "Anonimowy" + getSquareBracketStart() + "<red>" + incognitoIdentifier + getSquareBracketEnd();
    }

    public static String formatGuildTag(Guild guild) {
        return getSquareBracketStart() + "<light_purple>" + guild.getTag() + getSquareBracketEnd();
    }

    public static String formatGuildTagAndName(Guild guild) {
        return formatGuildTag(guild) + "<dark_gray>, <light_purple>" + guild.getName();
    }

    public static Optional<String> formatGuildTag(Player player, @Nullable Guild guild, @Nullable Guild otherGuild) {
        if (guild == null) {
            return Optional.empty();
        }

        String color;
        if (guild.isMember(player.getUniqueId())) {
            color = "<green>";
        } else if (otherGuild != null && guild.getAlliances().contains(otherGuild.getTag())) {
            color = "<gold>";
        } else {
            color = "<red>";
        }

        return Optional.of(
                getSquareBracketStart() + color + guild.getTag() + getSquareBracketEnd()
        );
    }

    public static String formatDeathType(MurderType murderType) {
        return switch (murderType) {
            case DEFAULT -> "Umarłeś/aś!";
            case WHILE_VICTIM_EATING -> "Polizałeś/aś!";
            case WHILE_VICTIM_RUNNING -> "Umarłeś/aś podczas ucieczki!";
            case REVENGE -> "Zemsta";
        };
    }

    public static String formatDeathTypeInMessage(MurderType murderType) {
        return switch (murderType) {
            case DEFAULT -> "został/a zabity/a przez";
            case WHILE_VICTIM_EATING -> "polizał/a przez";
            case WHILE_VICTIM_RUNNING -> "próbował/a uciec przed";
            case REVENGE -> "pożałował/a zabijając";
        };
    }

    /* Messages */
    public static String formatBanMessage(Punishment punishment) {
        return colored(String.join("\n", List.of(
                "&8&m--------------------",
                "&8● &4Zbanowano cię! &8●",
                "",
                "&8● &cPowód: &6" + punishment.getReason(),
                "&8● &cAdministrator&8: &6" + punishment.getAddedBy(),
                "&8● &cWygasa&8: &6" + (punishment.isPermanent() ? "nigdy" : "za " + TimeUtil.instantToString(punishment.getExpireAt().toInstant(), true)) + "&8.",
                "&8● &cData utworzenia&8: &6" + TimeUtil.format(punishment.getCreatedAt().toInstant()),
                "",
                "&8&m--------------------"
        )));
    }

    public static String formatPrivateMessage(String player, String received, String message) {
        return getSquareBracketStart() + "<light_purple>" + player + " <dark_gray>-> <light_purple>" + received + StyleUtil.getSquareBracketEnd() + "<dark_gray>: <white>" + message;
    }

    public static String formatPunishmentMessage(PunishmentType type) {
        return getSquareBracketStart() + getRedGradient() + type.name().toUpperCase() + getSquareBracketEnd();
    }

    public static String formatDeathMessage(String victim, int removedPoints, MurderType murderType, String killer, int addedPoints) {
        return getDeath() + " <red>Gracz <red>" + victim + " " + getSquareBracketStart() + "<red>-" + removedPoints +  getSquareBracketEnd() + " <red>" + formatDeathTypeInMessage(murderType) + " " + killer + " " + getSquareBracketStart() + "<green>+" + addedPoints + getSquareBracketEnd() + "<dark_gray>.";
    }
}

package me.dmk.core.util.string;

import lombok.experimental.UtilityClass;
import me.dmk.core.guild.Guild;
import me.dmk.core.profile.punishment.Punishment;
import me.dmk.core.util.TimeUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

/**
 * Created by DMK on 30.12.2022
 */

@UtilityClass
public class StringFormatter {

    public static String formatLong(long i, String single, String second, String many) {
        long iDivided = i % 10L;

        return (i == 1 ? single : (i < 5 || i > 20 && iDivided < 5 && iDivided != 1) ? second : many);
    }

    public static String formatOpeningSquareBracket() {
        return "<dark_gray>[</dark_gray>";
    }

    public static String formatClosingSquareBracket() {
        return "<dark_gray>]</dark_gray>";
    }

    public static String formatPurpleGradient() {
        return "<gradient:light_purple:dark_purple>";
    }

    public static String formatGreenGradient() {
        return "<gradient:green:dark_green>";
    }

    public static String formatRedGradient() {
        return "<gradient:red:dark_red>";
    }

    public static String formatLocation(Location location) {
        return "<light_purple>" + location.getBlockX() + "x<dark_gray>, <light_purple>" + location.getBlockY() + "y<dark_gray>, <light_purple>" + location.getBlockZ() + "z";
    }

    public static String formatSuccess() {
        return formatOpeningSquareBracket() + SymbolUtil.getCheckMark("<green>") + formatClosingSquareBracket();
    }

    public static String formatError() {
        return formatOpeningSquareBracket() + SymbolUtil.getCrossMark("<red>") + formatClosingSquareBracket();
    }

    public static String formatWarning() {
        return formatOpeningSquareBracket() + SymbolUtil.getExclamationMark("<gold>") + formatClosingSquareBracket();
    }

    public static String formatBoolean(boolean status) {
        return (status ? formatGreenGradient() + "włączony" : formatRedGradient() + "wyłączony") + "</gradient>";
    }

    public static String formatBoolean(boolean status, char symbol) {
        return (status ? formatGreenGradient() + "włączon" : formatRedGradient() + "wyłączon") + symbol + "</gradient>";
    }

    public static String formatBooleanYesOrNo(boolean status) {
        return (status ? "<green>Tak" : "<red>Nie");
    }

    public static String formatIncognito(String incognitoIdentifier) {
        return "Anonimowy" + formatOpeningSquareBracket() + "<red>" + incognitoIdentifier + formatClosingSquareBracket();
    }

    public static String formatGuild() {
        return formatOpeningSquareBracket() + "<green>GILDIA" + formatClosingSquareBracket();
    }

    public static String formatAlliance() {
        return formatOpeningSquareBracket() + "<gold>SOJUSZNICY" + formatClosingSquareBracket();
    }


    public static String formatVanish() {
        return formatOpeningSquareBracket() + "<light_purple>V" + formatClosingSquareBracket();
    }

    public static Optional<String> formatGuildTag(Guild guild, Player other, Guild otherGuild) {
        if (guild == null) {
            return Optional.empty();
        }

        String color;
        if (guild.isMember(other.getUniqueId())) {
            color = "<green>";
        } else if (otherGuild != null && guild.getAlliances().contains(otherGuild.getTag())) {
            color = "<gold>";
        } else {
            color = "<red>";
        }

        return Optional.of(
                formatOpeningSquareBracket() + color + guild.getTag() + formatClosingSquareBracket()
        );
    }

    public static String formatBanMessage(Punishment punishment) {
        return StringUtil.colorLegacy(String.join("\n", List.of(
                "&8&m--------------------",
                "&8● &4Zbanowano cię! &8●",
                "",
                "&8● &cPowód: &6" + punishment.getReason(),
                "&8● &cAdministrator&8: &6" + punishment.getAddedBy(),
                "&8● &cWygasa&8: &6" + (punishment.isPermanent() ? "nigdy" : "za " + TimeUtil.instantToString(punishment.getExpireAt().toInstant(), true)) + "&8.",
                "&8● &cData utworzenia&8: &6" + TimeUtil.formatDate(punishment.getCreatedAt().toInstant()),
                "",
                "&8&m--------------------"
                ))
        );
    }

    public static String formatPrivateMessage(String senderName, String receivingName, String message) {
        return formatOpeningSquareBracket() + SymbolUtil.getEnvelope("<light_purple>") + formatClosingSquareBracket()
                + " " + senderName + " <dark_gray>-> " + receivingName + "<dark_gray>: <white>" + message;
    }
}

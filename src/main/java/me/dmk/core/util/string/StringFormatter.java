package me.dmk.core.util.string;

import lombok.experimental.UtilityClass;
import me.dmk.core.guild.Guild;
import me.dmk.core.profile.punishment.Punishment;
import me.dmk.core.util.TimeUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

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

    public static String formatSuccess() {
        return StringUtil.getOpeningSquareBracket() + "<green>" + SymbolUtil.getCheckMark() + StringUtil.getClosingSquareBracket();
    }

    public static String formatError() {
        return StringUtil.getOpeningSquareBracket() + "<red>" + SymbolUtil.getCrossMark() + StringUtil.getClosingSquareBracket();
    }

    public static String formatWarning() {
        return StringUtil.getOpeningSquareBracket() + "<gold>" + SymbolUtil.getExclamationMark() + StringUtil.getClosingSquareBracket();
    }

    public static String formatBoolean(boolean status) {
        return (status ? StringUtil.getGreenGradient() + "włączony" : StringUtil.getRedGradient() + "wyłączony") + "</gradient>";
    }

    public static String formatBoolean(boolean status, char symbol) {
        return (status ? StringUtil.getGreenGradient() + "włączon" : StringUtil.getRedGradient() + "wyłączon") + symbol + "</gradient>";
    }

    public static String formatIncognito(String incognitoIdentifier) {
        return "Anonimowy" + StringUtil.getOpeningSquareBracket() + "<red>" + incognitoIdentifier + StringUtil.getClosingSquareBracket();
    }

    public static String formatGuild() {
        return StringUtil.getOpeningSquareBracket() + "<green>GILDIA" + StringUtil.getClosingSquareBracket();
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
                StringUtil.getOpeningSquareBracket() + color + guild.getTag() + StringUtil.getClosingSquareBracket()
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
                "&8● &cData utworzenia&8: &6" + TimeUtil.format(punishment.getCreatedAt().toInstant()),
                "",
                "&8&m--------------------"
                ))
        );
    }

    public static String formatPrivateMessage(String senderName, String receivingName, String message) {
        return StringUtil.getOpeningSquareBracket() + "<light_purple>" + SymbolUtil.getEnvelope() + StringUtil.getClosingSquareBracket()
                + " " + senderName + " <dark_gray>-> " + receivingName + "<dark_gray>: " + message;
    }
}

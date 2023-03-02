package me.dmk.core.util;

import lombok.experimental.UtilityClass;
import me.dmk.core.util.string.StringFormatter;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

/**
 * Created by DMK on 29.12.2022
 */

@UtilityClass
public class TimeUtil {

    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy 'o' HH:mm:ss");
    private static final SimpleDateFormat timeForamt = new SimpleDateFormat("HH:mm");

    public static String durationToString(Duration duration) {
        if (duration.isNegative() || duration.isZero()) {
            return "<1s";
        }

        long millis = duration.toMillis();
        long seconds = duration.toSecondsPart();
        long minutes = duration.toMinutesPart();
        long hours = duration.toHoursPart();
        long days = duration.toDays();

        StringBuilder stringBuilder = new StringBuilder();

        if (days > 0) {
            stringBuilder.append(days)
                    .append(" ")
                    .append(StringFormatter.formatLong(days, "dzień", "dni", "dni"))
                    .append(", ");
        }

        if (hours > 0) {
            stringBuilder.append(hours)
                    .append(" ")
                    .append(StringFormatter.formatLong(hours, "godzinę", "godziny", "godzin"))
                    .append(", ");
        }

        if (minutes > 0) {
            stringBuilder.append(minutes)
                    .append(" ")
                    .append(StringFormatter.formatLong(minutes, "minutę", "minuty", "minut"))
                    .append(", ");
        }

        if (seconds > 0) {
            stringBuilder.append(seconds)
                    .append(" ")
                    .append(StringFormatter.formatLong(seconds, "sekundę", "sekundy", "sekund"));
        }

        if (stringBuilder.isEmpty() && millis > 0) {
            stringBuilder.append(millis)
                    .append(" ")
                    .append("ms");
        }

        return stringBuilder.toString();
    }

    public static String instantToString(Instant instant, boolean future) {
        Instant now = Instant.now();

        Duration duration;
        if (future) {
            duration = Duration.between(now, instant);
        } else {
            duration = Duration.between(instant, now);
        }

        return durationToString(duration);
    }

    public static long toDays(Instant instant, boolean future) {
        Instant now = Instant.now();

        Duration duration;
        if (future) {
            duration = Duration.between(now, instant);
        } else {
            duration = Duration.between(instant, now);
        }

        return duration.toDays();
    }

    public static Optional<Instant> stringToInstant(String string) {
        if (string.isEmpty() || string.length() == 1) {
            return Optional.empty();
        }

        long subString;
        try {
            subString  = Long.parseLong(string.substring(0, string.length() - 1));
        } catch (NumberFormatException numberFormatException) {
            return Optional.empty();
        }

        char charAt = string.toUpperCase().charAt(string.length() - 1);

        return switch (charAt) {
            case 'S' -> Optional.of(Instant.now().plus(subString, ChronoUnit.SECONDS));
            case 'M' -> Optional.of(Instant.now().plus(subString, ChronoUnit.MINUTES));
            case 'H' -> Optional.of(Instant.now().plus(subString, ChronoUnit.HOURS));
            case 'D' -> Optional.of(Instant.now().plus(subString, ChronoUnit.DAYS));
            default -> Optional.empty();
        };
    }

    public static String formatDate(Date date) {
        return dateTimeFormat.format(date);
    }

    public static String formatDate(Instant instant) {
        return dateTimeFormat.format(Date.from(instant));
    }

    public static String formatTime(Instant instant) {
        return timeForamt.format(Date.from(instant));
    }
}

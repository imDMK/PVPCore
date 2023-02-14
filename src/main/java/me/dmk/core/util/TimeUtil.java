package me.dmk.core.util;

import lombok.experimental.UtilityClass;

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

    public static String durationToString(Duration duration, boolean milliseconds) {
        if (duration.isNegative()) {
            return "<1s";
        }

        long millis = duration.toMillis();
        long seconds = duration.toSeconds() % 60L;
        long minutes = duration.toMinutes() % 60L;
        long hours = duration.toHours() % 24L;
        long days = duration.toDays();

        StringBuilder stringBuilder = new StringBuilder();

        if (days > 0) {
            stringBuilder.append(days)
                    .append("dni")
                    .append(" ");
        }

        if (hours > 0) {
            stringBuilder.append(hours)
                    .append("godz")
                    .append(" ");
        }

        if (minutes > 0) {
            stringBuilder.append(minutes)
                    .append("min")
                    .append(" ");
        }

        if (seconds > 0) {
            stringBuilder.append(seconds)
                    .append("sec")
                    .append(" ");
        }

        if (stringBuilder.isEmpty() && milliseconds) {
            stringBuilder.append(millis)
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

        return durationToString(duration, true);
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


    public static String format(Instant instant) {
        return dateTimeFormat.format(Date.from(instant));
    }

    public static String formatTime(Instant instant) {
        return timeForamt.format(Date.from(instant));
    }
}

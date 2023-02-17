package me.dmk.core.command.argument.player;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.MultilevelArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import me.dmk.core.util.string.StringFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import panda.std.Result;

import java.util.List;

/**
 * Created by DMK on 30.12.2022
 */

@ArgumentName("location")
public class LocationArgument implements MultilevelArgument<Location> {

    private final Component unknownLocation;

    public LocationArgument(MiniMessage miniMessage) {
        this.unknownLocation = miniMessage.deserialize(StringFormatter.formatError() + " <red>Podano nieprawidłową lokalizację<dark_gray>.");
    }

    @Override
    public Result<Location, ?> parseMultilevel(LiteInvocation liteInvocation, String... arguments) {
        return Result.supplyThrowing(NumberFormatException.class, () -> {
            double x = Double.parseDouble(arguments[0]);
            double y = Double.parseDouble(arguments[1]);
            double z = Double.parseDouble(arguments[2]);

            return new Location(null, x, y, z);
        }).mapErr(exception -> this.unknownLocation);
    }

    @Override
    public int countMultilevel() {
        return 3;
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return List.of(
            Suggestion.multilevel("100", "100", "100"),
            Suggestion.multilevel("10", "5", "10"),
            Suggestion.multilevel("1500", "200", "1500")
        );
    }
}

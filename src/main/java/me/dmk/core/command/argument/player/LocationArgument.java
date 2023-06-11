package me.dmk.core.command.argument.player;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.MultilevelArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import panda.std.Result;

import java.util.List;

/**
 * Created by DMK on 30.12.2022
 */

@ArgumentName("co-ordinates")
public class LocationArgument implements MultilevelArgument<Location> {

    @Override
    public Result<Location, ?> parseMultilevel(LiteInvocation liteInvocation, String... arguments) {
        if (liteInvocation.sender().getHandle() instanceof Player player) {
            return Result.supplyThrowing(NumberFormatException.class, () -> {
                double x = Double.parseDouble(arguments[0]);
                double y = Double.parseDouble(arguments[1]);
                double z = Double.parseDouble(arguments[2]);

                return new Location(player.getWorld(), x, y, z);
            }).mapErr(exception -> StringFormatter.formatError() + " <red>Podano nieprawidłową lokalizację<dark_gray>.");
        }

        return Result.error("Nie możesz użyć tej komendy");
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

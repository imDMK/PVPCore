package me.dmk.core.command.argument.player;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import me.dmk.core.util.string.StringFormatter;
import panda.std.Option;
import panda.std.Result;

import java.util.List;

/**
 * Created by DMK on 30.12.2022
 */

@ArgumentName("integer")
public class IntegerArgument implements OneArgument<Integer> {

    @Override
    public Result<Integer, ?> parse(LiteInvocation liteInvocation, String argument) {
        return Option.supplyThrowing(NumberFormatException.class, () -> Integer.parseInt(argument))
                .filter(integer -> integer > 0)
                .toResult(() -> StringFormatter.formatError() + " <red>Podany argument nie jest liczbą");
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return Suggestion.of("5", "10", "50", "100", "500");
    }
}

package me.dmk.core.command.argument.player;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import me.dmk.core.command.implementation.admin.speed.SpeedType;
import me.dmk.core.util.string.StringFormatter;
import panda.std.Option;
import panda.std.Result;

import java.util.Arrays;
import java.util.List;

@ArgumentName("type")
public class SpeedTypeArgument implements OneArgument<SpeedType> {

    @Override
    public Result<SpeedType, ?> parse(LiteInvocation liteInvocation, String argument) {
        return Option.supplyThrowing(IllegalArgumentException.class, () -> SpeedType.valueOf(argument.toUpperCase()))
                .toResult(() -> StringFormatter.formatError() + " <red>Podano nieprawidłowy typ prędkości<dark_gray>.");
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return Arrays.stream(SpeedType.values())
                .map(SpeedType::name)
                .map(Suggestion::of)
                .toList();
    }
}

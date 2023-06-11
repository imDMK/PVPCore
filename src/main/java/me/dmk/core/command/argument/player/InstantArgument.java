package me.dmk.core.command.argument.player;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import me.dmk.core.util.TimeUtil;
import me.dmk.core.util.string.StringFormatter;
import panda.std.Option;
import panda.std.Result;

import java.time.Instant;
import java.util.List;

/**
 * Created by DMK on 30.12.2022
 */

@ArgumentName("time")
public class InstantArgument implements OneArgument<Instant> {

    @Override
    public Result<Instant, ?> parse(LiteInvocation invocation, String argument) {
        return Option.ofOptional(TimeUtil.stringToInstant(argument))
                .toResult(() -> StringFormatter.formatError() + " <red>Podano nieprawidłowy czas<dark_gray>.");
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return Suggestion.of("30m", "1h", "7d", "30d");
    }
}

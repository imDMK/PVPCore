package me.dmk.core.command.argument.player;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import me.dmk.core.util.TimeUtil;
import me.dmk.core.util.string.StringFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import panda.std.Result;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Created by DMK on 30.12.2022
 */

@ArgumentName("time")
public class InstantArgument implements OneArgument<Instant> {

    private final Component unknownTimeFormat;

    public InstantArgument(MiniMessage miniMessage) {
        this.unknownTimeFormat = miniMessage.deserialize(StringFormatter.formatError() + " <red>Podano nieprawidłowe formatowanie czasu<dark_gray>.");
    }

    @Override
    public Result<Instant, ?> parse(LiteInvocation invocation, String argument) {
        Optional<Instant> instant = TimeUtil.stringToInstant(argument);
        if (instant.isPresent()) {
            return Result.ok(instant.get());
        }
        return Result.error(this.unknownTimeFormat);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return Suggestion.of("30m", "1h", "7d", "30d");
    }
}

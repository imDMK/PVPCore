package me.dmk.core.command.argument.player;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.StringUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import panda.std.Result;

import java.util.List;

/**
 * Created by DMK on 30.12.2022
 */

@ArgumentName("integer")
public class IntegerArgument implements OneArgument<Integer> {

    private final Component isNotInteger;

    public IntegerArgument(MiniMessage miniMessage) {
        this.isNotInteger = miniMessage.deserialize(StringFormatter.formatError() + " <red>Podano nieprawidłową liczbę<dark_gray>.");
    }

    @Override
    public Result<Integer, ?> parse(LiteInvocation liteInvocation, String argument) {
        if (StringUtil.isInteger(argument)) {
            if (Integer.parseInt(argument) < 0) {
                return Result.error(this.isNotInteger);
            }
            return Result.ok(Integer.parseInt(argument));
        }

        return Result.error(this.isNotInteger);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return Suggestion.of("5", "10", "50", "100", "500");
    }
}

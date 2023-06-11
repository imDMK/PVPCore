package me.dmk.core.command.argument.player;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.amount.AmountValidator;
import dev.rollczi.litecommands.suggestion.Suggestion;
import me.dmk.core.util.string.StringFormatter;
import panda.std.Option;
import panda.std.Result;

import java.util.List;
import java.util.stream.IntStream;

@ArgumentName("speed")
public class SpeedArgument implements OneArgument<Integer> {

    private final AmountValidator SpeedValidator = AmountValidator.none().min(0).max(10);

    @Override
    public Result<Integer, ?> parse(LiteInvocation liteInvocation, String argument) {
        return Option.supplyThrowing(NumberFormatException.class, () -> Integer.parseInt(argument))
                .filter(SpeedValidator::valid)
                .toResult(() -> StringFormatter.formatError() + " <red>Podano nieprawidłową prędkość<dark_gray>.");
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return IntStream.range(0, 11)
                .mapToObj(String::valueOf)
                .map(Suggestion::of)
                .toList();
    }
}

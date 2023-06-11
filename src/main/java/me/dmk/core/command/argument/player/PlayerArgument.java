package me.dmk.core.command.argument.player;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import panda.std.Option;
import panda.std.Result;

import java.util.List;

/**
 * Created by DMK on 16.01.2023
 */

@ArgumentName("player")
public class PlayerArgument implements OneArgument<Player> {

    @Override
    public Result<Player, ?> parse(LiteInvocation liteInvocation, String argument) {
        return Option.of(Bukkit.getPlayer(argument))
                .toResult(() -> StringFormatter.formatError() + " <red>Podany gracz nie istnieje<dark_gray>.");
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .map(Suggestion::of)
                .toList();
    }
}

package me.dmk.core.command.argument.player;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import me.dmk.core.util.StringUtil;
import me.dmk.core.util.StyleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.GameMode;
import panda.std.Option;
import panda.std.Result;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by DMK on 29.12.2022
 */

@ArgumentName("type")
public class GameModeArgument implements OneArgument<GameMode> {

    private final String[] gameModes = {
            "0", "1", "2", "3",
            GameMode.SURVIVAL.name(),
            GameMode.CREATIVE.name(),
            GameMode.ADVENTURE.name(),
            GameMode.SPECTATOR.name()
    };

    private final Component unknownGameModeType;

    public GameModeArgument(MiniMessage miniMessage) {
        this.unknownGameModeType = miniMessage.deserialize(StyleUtil.getError() + " <red>Podano nieprawidłowy typ gry<dark_gray>.");
    }

    @Override
    public Result<GameMode, ?> parse(LiteInvocation invocation, String argument) {
        if (StringUtil.isInteger(argument)) {
            AtomicReference<GameMode> atomicReference = new AtomicReference<>();

            switch (Integer.parseInt(argument)) {
                case 0 -> atomicReference.set(GameMode.SURVIVAL);
                case 1 -> atomicReference.set(GameMode.CREATIVE);
                case 2 -> atomicReference.set(GameMode.ADVENTURE);
                case 3 -> atomicReference.set(GameMode.SPECTATOR);
                default -> atomicReference.set(null);
            }

            if (atomicReference.get() == null) {
                return Result.error(this.unknownGameModeType);
            }

            return Result.ok(atomicReference.get());
        }

        Option<GameMode> gameMode = Option.supplyThrowing(IllegalArgumentException.class,
                () -> GameMode.valueOf(argument.toUpperCase())
        );

        if (gameMode.isPresent()) {
            return Result.ok(gameMode.get());
        }

        return Result.error(this.unknownGameModeType);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return Suggestion.of(this.gameModes);
    }
}

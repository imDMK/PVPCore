package me.dmk.core.command.argument.player;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import me.dmk.core.util.string.StringFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.GameMode;
import panda.std.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DMK on 29.12.2022
 */

@ArgumentName("type")
public class GameModeArgument implements OneArgument<GameMode> {

    private final Component unknownGameModeType;
    private final Map<String, GameMode> gameModes = new HashMap<>();

    public GameModeArgument(MiniMessage miniMessage) {
        this.unknownGameModeType = miniMessage.deserialize(StringFormatter.formatError() + " <red>Podano nieprawidłowy typ gry<dark_gray>.");

        for (GameMode gameMode : GameMode.values()) {
            this.gameModes.put(gameMode.name().toUpperCase(), gameMode);
        }

        this.gameModes.put("0", GameMode.SURVIVAL);
        this.gameModes.put("1", GameMode.CREATIVE);
        this.gameModes.put("2", GameMode.ADVENTURE);
        this.gameModes.put("3", GameMode.SPECTATOR);
    }

    @Override
    public Result<GameMode, ?> parse(LiteInvocation invocation, String argument) {
        GameMode gameMode = this.gameModes.get(argument.toUpperCase());

        if (gameMode == null) {
            return Result.error(this.unknownGameModeType);
        }

        return Result.ok(gameMode);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return Suggestion.of(this.gameModes.keySet());
    }
}

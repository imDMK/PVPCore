package me.dmk.core.command.implementation.admin;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "gamemode", aliases = "gm")
@Permission("core.command.gamemode")
public class GameModeCommand {

    private final NotificationController notificationController;

    @Execute(required = 1)
    void execute(Player player, @Arg GameMode gameMode) {
        if (player.getGameMode().equals(gameMode)) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Posiadasz już ten tryb gry<dark_gray>."
            );
            return;
        }

        player.setGameMode(gameMode);

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringFormatter.formatGreenGradient() + " Zmieniono</gradient> <gray>twój tryb gry na <light_purple>" + gameMode.name().toUpperCase() + "<dark_gray>."
        );
    }

    @Execute(required = 2)
    @Permission("core.command.gamemode.other")
    void execute(Player player, @Arg GameMode gameMode, @Arg @Name("player") Player other) {
        if (other.getGameMode().equals(gameMode)) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Gracz posiada już ten tryb gry<dark_gray>."
            );
            return;
        }

        other.setGameMode(gameMode);

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringFormatter.formatGreenGradient() + " Zmieniono</gradient> <gray>tryb gry gracza <light_purple>" + other.getName() + " <gray>na <light_purple>" + gameMode.name().toUpperCase() + "<dark_gray>."
        );
    }
}

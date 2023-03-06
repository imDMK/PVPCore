package me.dmk.core.command.implementation.admin;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.StringUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "teleport", aliases = "tp")
@Permission("core.command.teleport")
public class TeleportCommand {

    private final NotificationController notificationController;

    @Execute(required = 1)
    void execute(Player player, @Arg @Name("to") Player other) {
        player.teleport(other);
        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringUtil.getGreenGradient() + " Przeteleportowano</gradient> <gray>do gracza <light_purple>" + other.getName() + "<dark_gray>."
        );
    }

    @Execute(required = 3)
    void execute(Player player, @Arg Location location) {
        player.teleport(location);
        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringUtil.getGreenGradient() + " Przeteleportowano</gradient> <gray>cię na koordynaty " + StringFormatter.formatLocation(location) + "<dark_gray>."
        );
    }

    @Execute(required = 2)
    @Permission("core.teleport.other")
    void execute(Player player, @Arg @Name("player") Player other, @Arg @Name("to") Player other2) {
        other.teleport(other2);
        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringUtil.getGreenGradient() + " Przeteleportowano</gradient> <gray>gracza <light_purple>" + other.getName() + " <gray>do gracza <light_purple>" + other2.getName() + "<dark_gray>."
        );
    }

    @Execute(required = 4)
    @Permission("core.teleport.other")
    void execute(Player player, @Arg @Name("player") Player other, @Arg Location location) {
        other.teleport(location);
        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringUtil.getGreenGradient() + " Przeteleportowano</gradient> <gray>gracza <light_purple>" + other.getName() + " <gray>na kordynaty " + StringFormatter.formatLocation(location) + "<dark_gray>."
        );
    }
}

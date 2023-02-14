package me.dmk.core.command.implementation.admin;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.util.StyleUtil;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "fly")
@Permission("core.command.fly")
public class FlyCommand {

    private final NotificationController notificationController;

    @Execute(required = 0)
    void execute(Player player) {
        player.setAllowFlight(!player.getAllowFlight());

        this.notificationController.sendMessage(player,
                StyleUtil.getSuccess() + " <gray>Twój tryb latania został " + StyleUtil.formatBoolean(player.getAllowFlight()) + "<dark_gray>."
        );
    }

    @Execute(required = 1)
    @Permission("core.command.fly.other")
    void execute(Player player, @Arg @Name("player") Player other) {
        other.setAllowFlight(!other.getAllowFlight());

        this.notificationController.sendMessage(player,
                StyleUtil.getSuccess() + " <gray>Tryb latania gracza <light_purple>" + other.getName() + " <gray>został " + StyleUtil.formatBoolean(other.getAllowFlight()) + "<dark_gray>."
        );
    }
}

package me.dmk.core.command.implementation.admin;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "speed")
@Permission("core.command.speed")
public class SpeedCommand {

    private final NotificationController notificationController;

    @Execute(required = 1)
    void execute(Player player, @Arg @Name("speed") Integer speed) {
        if (speed < 1 || speed > 10) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Szybkość nie może być niższa niż 1 oraz większa niż 10<dark_gray>."
            );
            return;
        }

        if (player.isFlying()) {
            player.setFlySpeed(speed / 10.0f);
        } else {
            player.setWalkSpeed(speed / 10.0f);
        }

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringFormatter.formatGreenGradient() + " Zmieniono</gradient> <gray>twoją prędkość <light_purple>" + (player.isFlying() ? "latania" : "chodzenia") + " <gray>na <light_purple>" + speed + "<dark_gray>."
        );
    }

    @Execute(required = 2)
    @Permission("core.command.speed.other")
    void execute(Player player, @Arg @Name("speed") Integer speed, @Arg @Name("player") Player other) {
        if (speed < 1 || speed > 10) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Szybkość nie może być niższa niż 1 oraz większa niż 10<dark_gray>."
            );
            return;
        }

        if (other.isFlying()) {
            other.setFlySpeed(speed / 10.0f);
        } else {
            other.setWalkSpeed(speed / 10.0f);
        }

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringFormatter.formatGreenGradient() + " Zmieniono</gradient> <gray>prędkość <light_purple>" + (player.isFlying() ? "latania" : "chodzenia") + " <gray>gracza <light_purple>" + other.getName() + " <gray>na <light_purple>" + speed + "<dark_gray>."
        );
    }
}

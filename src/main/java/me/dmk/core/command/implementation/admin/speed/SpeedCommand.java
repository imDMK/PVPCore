package me.dmk.core.command.implementation.admin.speed;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.By;
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
    void execute(Player player, @Arg @By("speed") Integer speed) {
        this.changeSpeed(player, speed);

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringFormatter.formatGreenGradient() + " Zmieniono</gradient> <gray>twoją prędkość <light_purple>" + (player.isFlying() ? "latania" : "chodzenia") + " <gray>na <light_purple>" + speed + "<dark_gray>."
        );
    }

    @Execute(required = 2)
    @Permission("core.command.speed.other")
    void execute(Player player, @Arg @By("speed") Integer speed, @Arg @Name("player") Player other) {
        this.changeSpeed(player, speed);

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringFormatter.formatGreenGradient() + " Zmieniono</gradient> <gray>prędkość <light_purple>" + (player.isFlying() ? "latania" : "chodzenia") + " <gray>gracza <light_purple>" + other.getName() + " <gray>na <light_purple>" + speed + "<dark_gray>."
        );
    }

    @Execute(required = 2)
    void execute(Player player, @Arg SpeedType speedType, @Arg @By("speed") Integer speed) {
        this.changeSpeed(player, speedType, speed);

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringFormatter.formatGreenGradient() + " Zmieniono</gradient> <gray>twoją prędkość <light_purple>" + (speedType == SpeedType.WALK ? "chodzenia" : "latania") + " <gray>na <light_purple>" + speed + "<dark_gray>."
        );
    }

    @Execute(required = 3)
    @Permission("core.command.speed.other")
    void execute(Player player, @Arg SpeedType speedType, @Arg @By("speed") Integer speed, Player other) {
        this.changeSpeed(other, speedType, speed);

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringFormatter.formatGreenGradient() + " Zmieniono</gradient> <gray>prędkość <light_purple>" + (speedType == SpeedType.WALK ? "chodzenia" : "latania") + " <gray>gracza <light_purple>" + other.getName() + " <gray>na <light_purple>" + speed + "<dark_gray>."
        );
    }

    void changeSpeed(Player player, int speed) {
        if (player.isFlying()) {
            player.setFlySpeed(speed / 10.0f);
        }
        else {
            player.setWalkSpeed(speed / 10.0f);
        }
    }

    void changeSpeed(Player player, SpeedType speedType, int speed) {
        switch (speedType) {
            case WALK -> player.setWalkSpeed(speed / 10.0f);
            case FLY -> player.setFlySpeed(speed / 10.0f);
            default -> throw new IllegalStateException("Unexpected speedType value: " + speedType);
        }
    }
}

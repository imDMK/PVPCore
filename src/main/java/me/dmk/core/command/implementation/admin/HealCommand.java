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

@Route(name = "heal", aliases = "feed")
@Permission("core.command.heal")
public class HealCommand {

    private final NotificationController notificationController;

    @Execute(required = 0)
    void execute(Player player) {
        this.heal(player);

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringFormatter.formatGreenGradient() + " Uleczono</gradient> <gray>cię<dark_gray>."
        );
    }

    @Execute(required = 1)
    @Permission("core.command.heal.other")
    void execute(Player player, @Arg @Name("player") Player other) {
        this.heal(other);

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringFormatter.formatGreenGradient() + " Uleczono</gradient> <gray>gracza <light_purple>" + other.getName() + "<dark_gray>."
        );
    }

    private void heal(Player player) {
        player.setHealth(20.D);
        player.setFireTicks(0);
        player.setFoodLevel(20);
    }
}

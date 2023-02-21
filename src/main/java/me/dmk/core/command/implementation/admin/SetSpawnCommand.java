package me.dmk.core.command.implementation.admin;

import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.StringUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 21.02.2023
 */

@AllArgsConstructor

@Route(name = "setspawn")
@Permission("core.command.setspawn")
public class SetSpawnCommand {

    private final NotificationController notificationController;

    @Async
    @Execute
    void execute(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();

        world.setSpawnLocation(location);

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringUtil.getGreenGradient() + " Ustawiony <gray>nowy spawn na " + StringFormatter.formatLocation(location) + "<dark_gray>."
        );
    }
}

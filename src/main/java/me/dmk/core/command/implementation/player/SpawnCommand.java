package me.dmk.core.command.implementation.player;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.teleport.TeleportMap;
import me.dmk.core.teleport.TeleportRequest;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.StringUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 20.02.2023
 */

@AllArgsConstructor

@Route(name = "spawn")
public class SpawnCommand {

    private final NotificationController notificationController;
    private final TeleportMap teleportMap;

    @Async
    @Execute(required = 0)
    void execute(Player player) {
        Location spawn = player.getWorld().getSpawnLocation();

        if (this.teleportMap.isTeleporting(player)) {
            this.teleportMap.removeTeleporting(player);

            this.notificationController.sendActionBar(player, "<red>Anulowano teleportację.");
            return;
        }

        this.notificationController.sendActionBar(player, "<green>Rozpoczynanie teleportacji...");
        new TeleportRequest(player, spawn);
    }

    @Execute(required = 1)
    @Permission("core.command.spawn.other")
    void execute(Player player, @Arg Player other) {
        Location spawn = other.getWorld().getSpawnLocation();

        other.teleport(spawn);

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringUtil.getGreenGradient() + " Przeteleportowano</gradient> <gray>gracza <light_purple>" + other.getName() + " <gray>na spawn<dark_gray>."
        );
    }
}

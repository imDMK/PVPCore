package me.dmk.core.teleport;

import me.dmk.core.CorePlugin;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.task.BukkitTask;
import me.dmk.core.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.Duration;

/**
 * Created by DMK on 20.02.2023
 */

public class TeleportRequest extends BukkitTask {

    private final NotificationController notificationController = CorePlugin.getCorePlugin().getNotificationController();
    private final TeleportMap teleportMap = CorePlugin.getCorePlugin().getTeleportMap();

    private final Player player;
    private final Location location;

    public TeleportRequest(Player player, Location location) {
        super(0L, 5L);
        this.player = player;
        this.location = location;

        this.teleportMap.addTeleporting(player);
    }

    public boolean cannotBeTeleported() {
        if (!this.player.isOnline()) {
            return true;
        }

        return !this.teleportMap.isTeleporting(this.player);
    }

    @Override
    public void onRun(long time) {
        if (this.cannotBeTeleported()) {
            this.notificationController.sendActionBar(this.player, "<red>Anulowano teleportację.");
            this.setCanceled(true);
            return;
        }

        if (this.player.hasPermission("core.teleport.delay.bypass")) {
            onFinish();
            this.setCanceled(true);
            return;
        }

        this.notificationController.sendActionBar(
                this.player,
                "<green>Teleportacja za " + TimeUtil.durationToString(Duration.ofSeconds(time))
        );
    }

    @Override
    public void onFinish() {
        if (this.cannotBeTeleported()) {
            this.notificationController.sendActionBar(this.player, "<red>Anulowano teleportację.");
            this.setCanceled(true);
            return;
        }

        Bukkit.getScheduler().runTaskLater(
                CorePlugin.getCorePlugin(),
                () -> this.player.teleport(this.location),
                2L
        ); //Player teleports cannot be triggered asynchronously

        this.teleportMap.removeTeleporting(this.player);

        this.notificationController.sendActionBar(
                this.player,
                "<green>Przeteleportowano."
        );
    }
}

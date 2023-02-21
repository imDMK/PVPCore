package me.dmk.core.command.implementation.player;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.task.BukkitTask;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DMK on 17.01.2023
 */

@AllArgsConstructor

@Route(name = "ping")
public class PingCommand {

    private final NotificationController notificationController;

    @Execute(required = 0)
    void execute(Player player) {
        int ping = player.getPing();
        String message = this.colorPing(ping);

        this.notificationController.sendMessage(player,
                StringFormatter.formatWarning() + " <gray>Twój ping jest " + message
        );
    }

    @Execute(required = 1)
    void execute(Player player, @Arg Player other) {
        int ping = other.getPing();
        String message = this.colorPing(ping);

        this.notificationController.sendMessage(player,
                StringFormatter.formatWarning() + " <gray>Ping gracza <light_purple>" + other.getName() + " <gray>jest " + message
        );
    }

    @Execute(route = "test", required = 0)
    void executeTest(Player player) {
        this.notificationController.sendMessage(player,
                StringFormatter.formatWarning() + " <green>Rozpoczynanie ping testu... "
        );

        List<Integer> pings = new ArrayList<>();

        new BukkitTask(20L, 10L) {
            @Override
            public void onRun(long time) {
                if (!player.isOnline()) {
                    setCanceled(true);
                    return;
                }

                int ping = player.getPing();
                String message = colorPing(ping);

                pings.add(ping);

                notificationController.sendMessage(player,
                        StringFormatter.formatWarning() + " <gray>Twój ping jest " + message
                );
            }

            @Override
            public void onFinish() {
                int sum = pings.stream().reduce(0, Integer::sum);
                int average = sum / pings.size();

                notificationController.sendMessage(player,
                        StringFormatter.formatWarning() + " <green>Zakończono test pingu<dark_gray>, <green>średni ping " + colorPing(average)
                );
            }
        };
    }

    private String colorPing(int ping) {
        if (ping > 150) {
            return "<red>bardzo wysoki <dark_gray>(<red>" + ping + "ms<dark_gray>).";
        } else if (ping > 80) {
            return "<yellow>wysoki <dark_gray>(<yellow>" + ping + "ms<dark_gray>).";
        } else if (ping < 40) {
            return "<green>bardzo niski <dark_gray>(<green>" + ping + "ms<dark_gray>).";
        } else {
            return "<green>niski <dark_gray>(<green>" + ping + "ms<dark_gray>).";
        }
    }
}

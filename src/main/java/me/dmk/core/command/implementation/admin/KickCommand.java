package me.dmk.core.command.implementation.admin;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.StringUtil;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "kick")
@Permission("core.command.kick")
public class KickCommand {

    private final NotificationController notificationController;

    @Execute(required = 1)
    void execute(Player player, @Arg @Name("player") Player other) {
        other.kickPlayer(
                StringUtil.colorLegacy("&cNie podano powodu.")
        );

        this.notificationController.sendGlobalMessage(
                StringFormatter.formatWarning() + " <gray>Gracz <light_purple>" + other.getName() + " <gray>został <red>wyrzucony <gray>przez <light_purple>" + player.getName() + "<dark_gray>.",
                "core.command.kick"
        );
    }

    @Execute(min = 2)
    void execute(Player player, @Arg @Name("player") Player other, @Joiner @Name("reason") String reason) {
        other.kickPlayer(
                StringUtil.colorLegacy("&c" + reason)
        );

        this.notificationController.sendGlobalMessage(
                StringFormatter.formatWarning() + " <gray>Gracz <light_purple>" + other.getName() + " <gray>został <red>wyrzucony <gray>przez <light_purple>" + player.getName() + " <gray>za <red>" + reason + "<dark_gray>.",
                "core.command.kick"
        );
    }
}

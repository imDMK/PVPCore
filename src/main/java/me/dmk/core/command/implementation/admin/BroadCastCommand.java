package me.dmk.core.command.implementation.admin;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.chat.notification.NotificationType;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "broadcast", aliases = "bc")
@Permission("core.command.broadcast")
public class BroadCastCommand {

    private final NotificationController notificationController;

    @Async
    @Execute(min = 2)
    void execute(CommandSender sender, @Arg NotificationType notificationType, @Joiner @Name("message") String message) {
        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();

        switch (notificationType) {
            case CHAT -> this.notificationController.sendMessage(players, message);
            case TITLE -> this.notificationController.sendTitle(players, message, "");
            case SUBTITLE -> this.notificationController.sendTitle(players, "", message);
        }

        this.notificationController.sendMessage(sender,
                StringFormatter.formatSuccess() + StringUtil.getGreenGradient() + " Wysłano</gradient> <gray>globalną wiadomość <light_purple>" + notificationType.name().toUpperCase() + "<dark_gray>."
        );
    }
}

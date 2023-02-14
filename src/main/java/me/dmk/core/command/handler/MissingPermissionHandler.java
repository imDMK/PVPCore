package me.dmk.core.command.handler;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.handle.PermissionHandler;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.util.StyleUtil;
import org.bukkit.command.CommandSender;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor
public class MissingPermissionHandler implements PermissionHandler<CommandSender> {

    private final NotificationController notificationController;

    @Override
    public void handle(CommandSender sender, LiteInvocation liteInvocation, RequiredPermissions requiredPermissions) {
        this.notificationController.sendMessage(sender,
                StyleUtil.getError() + " <red>Nie posiadasz uprawnień <dark_gray>(<gold>" + requiredPermissions.getPermissions().get(0) + "<dark_gray>)."
        );
    }
}

package me.dmk.core.command.handler;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.schematic.Schematic;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.util.StyleUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor
public class InvalidUsageHandler implements dev.rollczi.litecommands.handle.InvalidUsageHandler<CommandSender> {

    private final NotificationController notificationController;

    @Override
    public void handle(CommandSender sender, LiteInvocation liteInvocation, Schematic schematic) {
        List<String> schematics = schematic.getSchematics();

        if (sender instanceof Player) {
            if (schematics.size() == 1) {
                this.notificationController.sendMessage(sender,
                        StyleUtil.getError() + " <red>Poprawne użycie<dark_gray>: <gold>" + schematics.get(0)
                );
                return;
            }

            this.notificationController.sendMessage(sender, List.of(
                    StyleUtil.getError() + " <red>Poprawne użycie<dark_gray>:",
                    "<dark_gray>- <gold>" + String.join("\n<dark_gray>- <gold>", schematics)
            ));
        } else {
            sender.sendMessage("Poprawne użycie: " + (schematics.size() == 1 ? schematics.get(0) : String.join(", " + schematics)));
        }
    }
}

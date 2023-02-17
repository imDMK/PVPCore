package me.dmk.core.command.implementation.admin;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.StringUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "clear")
@Permission("core.command.clear")
public class ClearCommand {

    private final NotificationController notificationController;

    @Execute(required = 0)
    void execute(Player player) {
        Inventory inventory = player.getInventory();

        if (inventory.isEmpty()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Posiadasz pusty ekwipunek<dark_gray>."
            );
            return;
        }

        inventory.clear();
        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringUtil.getGreenGradient() + " Wyczyszczono</gradient> <gray>twój ekwipunek<dark_gray>."
        );
    }

    @Execute(required = 1)
    @Permission("core.command.clear.other")
    void execute(Player player, @Arg @Name("player") Player other) {
        Inventory inventory = other.getInventory();

        if (inventory.isEmpty()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Gracz posiada pusty ekwipunekdark_gray>."
            );
            return;
        }

        inventory.clear();
        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringUtil.getGreenGradient() + " Wyczyszczono</gradient> <gray>ekwipunek gracza <light_purple>" + other.getName() + "<dark_gray>."
        );
    }
}

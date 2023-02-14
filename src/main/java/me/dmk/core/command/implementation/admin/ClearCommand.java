package me.dmk.core.command.implementation.admin;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.util.StyleUtil;
import org.bukkit.entity.Player;

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
        if (player.getInventory().isEmpty()) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Posiadasz pusty ekwipunek<dark_gray>."
            );
            return;
        }

        player.getInventory().clear();
        this.notificationController.sendMessage(player,
                StyleUtil.getSuccess() + StyleUtil.getGreenGradient() + " Wyczyszczono</gradient> <gray>twój ekwipunek<dark_gray>."
        );
    }

    @Execute(required = 1)
    @Permission("core.command.clear.other")
    void execute(Player player, @Arg @Name("player") Player other) {
        if (other.getInventory().isEmpty()) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Gracz posiada pusty ekwipunekdark_gray>."
            );
            return;
        }

        other.getInventory().clear();
        this.notificationController.sendMessage(player,
                StyleUtil.getSuccess() + StyleUtil.getGreenGradient() + " Wyczyszczono</gradient> <gray>ekwipunek gracza <light_purple>" + other.getName() + "<dark_gray>."
        );
    }
}

package me.dmk.core.command.implementation.player;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.profile.Profile;
import me.dmk.core.util.TimeUtil;
import me.dmk.core.util.string.StringFormatter;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by DMK on 19.02.2023
 */

@AllArgsConstructor

@Route(name = "groups")
public class GroupsCommand {

    private final LuckPermsController luckPermsController;
    private final NotificationController notificationController;

    @Async
    @Execute(required = 0)
    void execute(Player player) {
        Collection<InheritanceNode> inheritanceNodeList = this.luckPermsController.getGroups(player.getUniqueId());
        if (inheritanceNodeList.isEmpty()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie posiadasz aktywnych grup<dark_gray>."
            );
            return;
        }

        this.notificationController.sendMessage(player,
                StringFormatter.formatWarning() + " <gray>Twoje <green>aktywne <gray>grupy<dark_gray>:"
        );

        inheritanceNodeList.forEach(node ->
                this.notificationController.sendMessage(player,
                        "<dark_gray>- <gray>Grupa <light_purple>" + node.getGroupName() + " <dark_gray>(<gray>wygasa " + (node.hasExpiry() ? "<light_purple>za " + TimeUtil.instantToString(node.getExpiry(), true) : "<green>nigdy") + "<dark_gray>),"
                )
        );
    }

    @Async
    @Execute(required = 1)
    void execute(Player player, @Arg Profile other) {
        Collection<InheritanceNode> inheritanceNodeList = this.luckPermsController.getGroups(other.getUuid());
        if (inheritanceNodeList.isEmpty()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Gracz nie posiada aktywnych group<dark_gray>."
            );
            return;
        }

        this.notificationController.sendMessage(player,
                StringFormatter.formatWarning() + " <green>Aktywne <gray>grupy gracza <light_purple>" + other.getName() + "<dark_gray>:"
        );

        inheritanceNodeList.forEach(node ->
                this.notificationController.sendMessage(player,
                        "<dark_gray>- <gray>Grupa <light_purple>" + node.getGroupName() + " <dark_gray>(<gray>wygasa " + (node.hasExpiry() ? "<light_purple>za " + TimeUtil.instantToString(node.getExpiry(), true) : "<green>nigdy") + "<dark_gray>),"
                )
        );
    }
}

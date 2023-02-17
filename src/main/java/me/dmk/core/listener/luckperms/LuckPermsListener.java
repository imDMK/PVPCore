package me.dmk.core.listener.luckperms;

import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.task.executor.TaskExecutor;
import me.dmk.core.util.TimeUtil;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.StringUtil;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 01.01.2023
 */

@AllArgsConstructor
public class LuckPermsListener {

    private final NotificationController notificationController;
    private final TaskExecutor taskExecutor;

    public void onNodeAdd(NodeAddEvent event) {
        if (!event.isUser()) {
            return;
        }

        User user = (User) event.getTarget();
        Node node = event.getNode();

        this.taskExecutor.runAsync(() -> {
            Player player = Bukkit.getServer().getPlayer(user.getUniqueId());
            if (player == null) {
                return;
            }

            if (node instanceof InheritanceNode) {
                String group = ((InheritanceNode) node).getGroupName();

                this.notificationController.sendMessage(player,
                        StringFormatter.formatWarning() + " <gray>Twój profil otrzymał nową grupę <light_purple>" + group.toUpperCase() + " <gray>na czas " +  StringUtil.getGreenGradient()  + (node.hasExpiry() ? TimeUtil.instantToString(node.getExpiry(), true) : "permanentny") + "</gradient><dark_gray>."
                );
            }
        });
    }

    public void onNodeRemove(NodeRemoveEvent event) {
        if (!event.isUser()) {
            return;
        }

        User user = (User) event.getTarget();
        Node node = event.getNode();

        this.taskExecutor.runAsync(() -> {
            Player player = Bukkit.getServer().getPlayer(user.getUniqueId());
            if (player == null) {
                return;
            }

            if (node instanceof InheritanceNode) {
                if (!node.hasExpired()) {
                    return;
                }

                String group = ((InheritanceNode) node).getGroupName();

                this.notificationController.sendMessage(player,
                        StringFormatter.formatWarning() + " <gray>Twoja grupa <light_purple>" + group.toUpperCase() + " " + StringUtil.getRedGradient() + "wygasła</gradient><dark_gray>."
                );
            }
        });
    }
}

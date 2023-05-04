package me.dmk.core.listener.luckperms;

import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.task.executor.TaskExecutor;
import me.dmk.core.util.TimeUtil;
import me.dmk.core.util.string.StringFormatter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
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
public class LuckPermsListener {

    private final NotificationController notificationController;
    private final TaskExecutor taskExecutor;

    public LuckPermsListener(NotificationController notificationController, TaskExecutor taskExecutor, LuckPerms luckPerms) {
        this.notificationController = notificationController;
        this.taskExecutor = taskExecutor;

        EventBus eventBus = luckPerms.getEventBus();
        eventBus.subscribe(NodeAddEvent.class, this::onNodeAdd);
        eventBus.subscribe(NodeRemoveEvent.class, this::onNodeRemove);
    }

    public void onNodeAdd(NodeAddEvent event) {
        if (!event.isUser()) {
            return;
        }

        User user = (User) event.getTarget();
        Node node = event.getNode();

        if (node instanceof InheritanceNode inheritanceNode) {
            this.taskExecutor.runAsync(() -> {
                Player player = Bukkit.getServer().getPlayer(user.getUniqueId());
                if (player == null) {
                    return;
                }

                String group = inheritanceNode.getGroupName();

                this.notificationController.sendMessage(player,
                        StringFormatter.formatWarning() + " <gray>Twój profil otrzymał nową grupę <light_purple>" + group.toUpperCase() + " <gray>na czas " + StringFormatter.formatGreenGradient() + (node.hasExpiry() ? TimeUtil.instantToString(node.getExpiry(), true) : "permanentny") + "</gradient><dark_gray>."
                );
            });
        }
    }

    public void onNodeRemove(NodeRemoveEvent event) {
        if (!event.isUser()) {
            return;
        }

        User user = (User) event.getTarget();
        Node node = event.getNode();

        if (node instanceof InheritanceNode inheritanceNode) {
            if (!node.hasExpired()) {
                return;
            }

            this.taskExecutor.runAsync(() -> {
                Player player = Bukkit.getServer().getPlayer(user.getUniqueId());
                if (player == null) {
                    return;
                }

                String group = inheritanceNode.getGroupName();

                this.notificationController.sendMessage(player,
                        StringFormatter.formatWarning() + " <gray>Twoja grupa <light_purple>" + group.toUpperCase() + " " + StringFormatter.formatRedGradient() + "wygasła</gradient><dark_gray>."
                );
            });
        }
    }
}

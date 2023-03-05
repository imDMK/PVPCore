package me.dmk.core.command.implementation.player;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.event.PrivateMessageEvent;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "message", aliases = "msg")
public class MessageCommand {

    private final NotificationController notificationController;
    private final ProfileCache profileCache;

    @Execute(min = 2)
    void execute(Player player, Profile profile, @Arg @Name("player") Player other, @Joiner @Name("message") String message) {
        Profile otherProfile = this.profileCache.getOrElseThrow(other);

        PrivateMessageEvent privateMessageEvent = new PrivateMessageEvent(player, profile, other, otherProfile, message);
        Bukkit.getPluginManager().callEvent(privateMessageEvent);

        if (privateMessageEvent.isCancelled()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " " + privateMessageEvent.getCancelMessage()
            );
        }
    }
}

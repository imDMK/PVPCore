package me.dmk.core.command.implementation.admin;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.GlobalChatCache;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.util.StyleUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "chat")
@Permission("core.command.chat")
public class ChatCommand {

    private final NotificationController notificationController;
    private final GlobalChatCache globalChatCache;

    @Async
    @Execute(route = "status")
    void execute(CommandSender sender) {
        this.globalChatCache.getGlobalChatSettings().switchStatus();

        this.notificationController.sendMessage(
                Bukkit.getOnlinePlayers(),
                StyleUtil.getWarning() + " <gray>Globalny czat został " + StyleUtil.formatBoolean(this.globalChatCache.getGlobalChatSettings().isEnabled()) + " <gray>przez <light_purple>" + sender.getName() + "<dark_gray>."
        );
    }

    @Async
    @Execute(route = "clear")
    void executeClear(CommandSender sender) {
        for (int i = 0; i < 100; i++) {
            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(""));
        }

        this.notificationController.sendMessage(
                Bukkit.getOnlinePlayers(),
                StyleUtil.getWarning() + " <gray>Globalny czat został " + StyleUtil.getPurpleGradient() + "wyczyszczony</gradient> <gray>przez <light_purple>" + sender.getName() + "<dark_gray>."
        );
    }

    @Execute(route = "delay", required = 1)
    void executeDelay(CommandSender sender, @Arg Integer seconds) {
        if (this.globalChatCache.getGlobalChatSettings().getDelay() == seconds) {
            this.notificationController.sendMessage(sender, StyleUtil.getError() + " <red>Opóźnienie nie zostało zmienione, ponieważ już jest ustawione na <gold>" + seconds + " <red>sekund/y<dark_gray>.");
            return;
        }

        this.globalChatCache.getGlobalChatSettings().setDelay(seconds);
        this.notificationController.sendMessage(
                Bukkit.getOnlinePlayers(),
                StyleUtil.getSilent() + " " + StyleUtil.getWarning() + " <gray>Administrator <light_purple>" + sender.getName() + " <gray>zmienił opóźnienie wysyłania globalnych wiadomości na <light_purple>" + seconds + " <gray>sekund<dark_gray>.",
                "core.command.chat"
        );
    }
}

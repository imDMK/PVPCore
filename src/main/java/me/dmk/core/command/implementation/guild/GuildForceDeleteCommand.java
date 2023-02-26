package me.dmk.core.command.implementation.guild;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.gui.confirmation.ConfirmationGui;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.task.executor.TaskExecutor;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.SymbolUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 05.02.2023
 */

@AllArgsConstructor

@Route(name = "guild forceDelete")
@Permission("core.command.guild.forcedelete")
public class GuildForceDeleteCommand {

    private final NotificationController notificationController;
    private final GuildController guildController;
    private final GuildCache guildCache;
    private final TaskExecutor taskExecutor;

    @Execute(min = 2)
    void execute(Player player, @Arg Guild guild, @Joiner @Name("reason") String reason) {
        new ConfirmationGui(player)
                .create(SymbolUtil.getCircle("<dark_gray>") + " <red>Potwierdź usunięcie gildii " + guild.getTag() + " " + SymbolUtil.getCircle("<dark_gray>"))
                .afterConfirm(event -> {
                    this.taskExecutor.runAsync(() -> {
                        this.guildCache.remove(guild);
                        this.guildController.delete(guild);
                    });

                    guild.getOnlineMembers().forEach(guildPlayer ->
                            this.notificationController.sendMessage(guildPlayer,
                                    StringFormatter.formatWarning() +  " <red>Twoja gildia została usunięta z powodu " + reason + "<dark_gray>."
                            )
                    );

                    this.notificationController.sendMessage(Bukkit.getOnlinePlayers(),
                            StringFormatter.formatWarning() + " <gray>Administrator <red>" + player.getName() + " <red>usunął <gray>gildię <red>" + guild.getTag() + " <gray>za <red>" + reason + "<dark_gray>.",
                            "core.command.guild.forcedelete"
                    );

                    player.closeInventory();
                })
                .closeAfterCancel()
                .open(true);
    }
}

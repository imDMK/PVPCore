package me.dmk.core.command.implementation.admin.punishment;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.punishment.Punishment;
import me.dmk.core.profile.punishment.PunishmentType;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.command.CommandSender;

import java.util.Date;
import java.util.Optional;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "unban")
@Permission("core.command.unban")
public class UnBanCommand {

    private final NotificationController notificationController;
    private final ProfileController profileController;

    @Async
    @Execute(required = 1)
    void execute(CommandSender sender, @Arg Profile profile) {
        Optional<Punishment> punishment = profile.getActivePunishment(PunishmentType.BAN);
        if (punishment.isEmpty()) {
            this.notificationController.sendMessage(sender,
                    StringFormatter.formatError() + " <red>Gracz nie jest zbanowany<dark_gray>."
            );
            return;
        }

        punishment.get().setRemoved(true);
        punishment.get().setRemovedBy(sender.getName());
        punishment.get().setRemovedAt(new Date());

        this.profileController.save(profile);

        this.notificationController.sendGlobalMessage(
                StringFormatter.formatWarning() + " <gray>Gracz <light_purple>" + profile.getName() + " <gray>został <green>odbanowany <gray>przez <light_purple>" + sender.getName() + "<dark_gray>.",
                "core.command.unban"
        );
    }
}

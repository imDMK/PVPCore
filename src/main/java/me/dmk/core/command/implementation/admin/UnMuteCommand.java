package me.dmk.core.command.implementation.admin;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.punishment.Punishment;
import me.dmk.core.profile.punishment.PunishmentType;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.util.StyleUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Date;
import java.util.Optional;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "unmute")
@Permission("core.command.unmute")
public class UnMuteCommand {

    private final NotificationController notificationController;
    private final ProfileController profileController;

    @Async
    @Execute(required = 1)
    void execute(CommandSender sender, @Arg Profile profile) {
        Optional<Punishment> punishment = profile.getActivePunishment(PunishmentType.MUTE);
        if (punishment.isEmpty()) {
            this.notificationController.sendMessage(sender,
                    StyleUtil.getError() + " <red>Gracz nie jest wyciszony<dark_gray>."
            );
            return;
        }

        punishment.get().setRemoved(true);
        punishment.get().setRemovedBy(sender.getName());
        punishment.get().setRemovedAt(new Date());

        this.profileController.save(profile);

        profile.getPlayer().ifPresent(p ->
                this.notificationController.sendMessage(p,
                        StyleUtil.getWarning() + StyleUtil.getGreenGradient() + " Odciszył </gradient><gray>cię administrator <light_purple>" + sender.getName() + "<dark_gray>."
                )
        );

        this.notificationController.sendMessage(
                Bukkit.getOnlinePlayers(),
                StyleUtil.getSilent() + " " + StyleUtil.getWarning() + " <gray>Gracz <light_purple>" + profile.getName() + " <gray>został " + StyleUtil.getGreenGradient() + "odciszony </gradient><gray>przez <light_purple>" + sender.getName() + "<dark_gray>.",
                "core.command.unban"
        );
    }
}

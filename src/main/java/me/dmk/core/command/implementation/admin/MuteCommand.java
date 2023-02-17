package me.dmk.core.command.implementation.admin;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.argument.joiner.Joiner;
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
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "mute")
@Permission("core.command.mute")
public class MuteCommand {

    private final NotificationController notificationController;
    private final ProfileController profileController;

    @Async
    @Execute(required = 1)
    void execute(CommandSender sender, @Arg Profile profile) {
        if (profile.getActivePunishment(PunishmentType.MUTE).isPresent()) {
            this.notificationController.sendMessage(sender,
                    StringFormatter.formatError() + " <red>Gracz jest już wyciszony<dark_gray>."
            );
            return;
        }

        String reason = "Nie podano powodu.";
        Punishment punishment = new Punishment(PunishmentType.MUTE, sender.getName(), reason);

        profile.getPunishments().add(punishment);
        this.profileController.save(profile);

        profile.getPlayer().ifPresent(p ->
                this.notificationController.sendMessage(p,
                        StringFormatter.formatWarning() + " <gradient:red:dark_red>Permanentnie wyciszono</gradient> <gray>cię przez <light_purple>" + sender.getName() + "<dark_gray>."
                )
        );

        this.notificationController.sendMessage(
                Bukkit.getOnlinePlayers(),
                StringFormatter.formatWarning() + " <gray>Gracz <light_purple>" + profile.getName() + " <gray>został <gradient:red:dark_red>permanentnie wyciszony</gradient> <gray>przez <light_purple>" + sender.getName() + "<dark_gray>.",
                "core.command.mute"
        );
    }

    @Execute(min = 2)
    void execute(CommandSender sender, @Arg Profile profile, @Joiner @Name("reason") String reason) {
        if (profile.getActivePunishment(PunishmentType.MUTE).isPresent()) {
            this.notificationController.sendMessage(sender,
                    StringFormatter.formatError() + " <red>Gracz jest już wyciszony<dark_gray>."
            );
            return;
        }

        Punishment punishment = new Punishment(PunishmentType.MUTE, sender.getName(), reason);

        profile.getPunishments().add(punishment);
        this.profileController.save(profile);

        profile.getPlayer().ifPresent(p ->
                this.notificationController.sendMessage(p,
                        StringFormatter.formatWarning() + " <gradient:red:dark_red>Permanentnie wyciszono</gradient> <gray>cię przez <light_purple>" + sender.getName() + " <gray>za <red>" + reason + "<dark_gray>."
                )
        );

        this.notificationController.sendMessage(
                Bukkit.getOnlinePlayers(),
                StringFormatter.formatWarning() + " <gray>Gracz <light_purple>" + profile.getName() + " <gray>został <gradient:red:dark_red>permanentnie wyciszony</gradient> <gray>przez <light_purple>" + sender.getName() + " <gray>za <red>" + reason + "<dark_gray>.",
                "core.command.mute"
        );
    }
}

package me.dmk.core.command.implementation.admin.punishment;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.CorePlugin;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.punishment.Punishment;
import me.dmk.core.profile.punishment.PunishmentType;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "tempban")
@Permission("core.command.tempban")
public class TempBanCommand {

    private final NotificationController notificationController;
    private final ProfileController profileController;

    @Async
    @Execute(required = 2)
    void execute(CommandSender sender, @Arg Profile profile, @Arg Instant instant) {
        if (profile.getActivePunishment(PunishmentType.BAN).isPresent()) {
            this.notificationController.sendMessage(sender,
                    StringFormatter.formatError() + " <red>Gracz jest już zbanowany<dark_gray>."
            );
            return;
        }

        String reason = "Nie podano powodu.";
        Punishment punishment = new Punishment(PunishmentType.BAN, sender.getName(), reason, instant);

        profile.getPunishments().add(punishment);
        this.profileController.save(profile);

        profile.getPlayer().ifPresent(p ->
                this.kickPlayer(p, punishment)
        );

        this.notificationController.sendGlobalMessage(
                StringFormatter.formatWarning() + " <gray>Gracz <light_purple>" + profile.getName() + " <gray>został <red>tymczasowo zbanowany <gray>przez <light_purple>" + sender.getName() + "<dark_gray>.",
                "core.command.tempban"
        );
    }

    @Async
    @Execute(min = 3)
    void execute(CommandSender sender, @Arg Profile profile, @Arg Instant instant, @Joiner @Name("reason") String reason) {
        if (profile.getActivePunishment(PunishmentType.BAN).isPresent()) {
            this.notificationController.sendMessage(sender,
                    StringFormatter.formatError() + " <red>Gracz jest już zbanowany<dark_gray>."
            );
            return;
        }

        Punishment punishment = new Punishment(PunishmentType.BAN, sender.getName(), reason, instant);

        profile.getPunishments().add(punishment);
        this.profileController.save(profile);

        profile.getPlayer().ifPresent(p ->
                this.kickPlayer(p, punishment)
        );

        this.notificationController.sendGlobalMessage(
                StringFormatter.formatWarning() + " <gray>Gracz <light_purple>" + profile.getName() + " <gray>został <red>tymczasowo zbanowany <gray>przez <light_purple>" + sender.getName() + " <gray>za <red>" + reason + "<dark_gray>.",
                "core.command.tempban"
        );
    }

    private void kickPlayer(Player player, Punishment punishment) {
        Bukkit.getScheduler().runTaskLater(
                CorePlugin.getCorePlugin(),
                () -> player.kickPlayer(StringFormatter.formatBanMessage(punishment)),
                2L
        );
    }
}

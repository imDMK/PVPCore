package me.dmk.core.command.implementation.admin.punishment;

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
import org.bukkit.command.CommandSender;

import java.util.Optional;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "ban")
@Permission("core.command.ban")
public class BanCommand {

    private final NotificationController notificationController;
    private final ProfileController profileController;

    @Async
    @Execute(required = 1)
    void execute(CommandSender sender, @Arg Profile profile) {
        Optional<Punishment> activeBanPunishment = profile.getActivePunishment(PunishmentType.BAN);
        if (activeBanPunishment.isPresent()) {
            this.notificationController.sendMessage(sender,
                    StringFormatter.formatError() + " <red>Podany gracz jest już zbanowany<dark_gray>."
            );
            return;
        }

        String reason = "Nie podano powodu.";
        Punishment punishment = new Punishment(PunishmentType.BAN, sender.getName(), reason);

        profile.getPunishments().add(punishment);
        this.profileController.save(profile);
        
        profile.getPlayer().ifPresent(p -> 
                p.kickPlayer(StringFormatter.formatBanMessage(punishment))
        );

        this.notificationController.sendGlobalMessage(
                StringFormatter.formatWarning() + " <gray>Gracz <light_purple>" + profile.getName() + " <gray>został <red>permanentnie zbanowany <gray>przez <light_purple>" + sender.getName() + "<dark_gray>.",
                "core.command.ban"
        );
    }

    @Async
    @Execute(min = 2)
    void execute(CommandSender sender, @Arg Profile profile, @Joiner @Name("reason") String reason) {
        Optional<Punishment> activeBanPunishment = profile.getActivePunishment(PunishmentType.BAN);
        if (activeBanPunishment.isPresent()) {
            this.notificationController.sendMessage(sender,
                    StringFormatter.formatError() + " <red>Podany gracz jest już zbanowany<dark_gray>."
            );
            return;
        }

        Punishment punishment = new Punishment(PunishmentType.BAN, sender.getName(), reason);

        profile.getPunishments().add(punishment);
        this.profileController.save(profile);

        profile.getPlayer().ifPresent(p ->
                p.kickPlayer(StringFormatter.formatBanMessage(punishment))
        );

        this.notificationController.sendGlobalMessage(
                StringFormatter.formatWarning() + " <gray>Gracz <light_purple>" + profile.getName() + " <gray>został <red>permanentnie zbanowany <gray>przez <light_purple>" + sender.getName() + " <gray>za <red>" + reason + "<dark_gray>.",
                "core.command.ban"
        );
    }
}

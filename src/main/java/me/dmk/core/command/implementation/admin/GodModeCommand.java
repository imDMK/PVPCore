package me.dmk.core.command.implementation.admin;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 08.03.2023
 */

@AllArgsConstructor

@Route(name = "godmode", aliases = "god")
@Permission("core.command.godmode")
public class GodModeCommand {

    private final NotificationController notificationController;
    private final ProfileCache profileCache;

    @Execute(required = 0)
    void execute(Player player, Profile profile) {
        ProfileSettings settings = profile.getProfileSettings();

        settings.setGod(!settings.isGod());

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + " <gray>Twój tryb nieśmiertelności został " + StringFormatter.formatBoolean(settings.isGod()) + "<dark_gray>."
        );
    }

    @Execute(required = 1)
    @Permission("core.command.godmode.other")
    void executeOther(Player player, @Arg @Name("player") Player other) {
        Profile profile = this.profileCache.getOrElseThrow(other);
        ProfileSettings settings = profile.getProfileSettings();

        settings.setGod(!settings.isGod());

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + " <gray>Tryb nieśmiertelności gracza <light_purple>" + other.getName() + " <gray>został " + StringFormatter.formatBoolean(settings.isGod()) + "<dark_gray>."
        );
    }
}

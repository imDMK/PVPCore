package me.dmk.core.command.implementation.player;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.settings.incognito.IncognitoController;
import me.dmk.core.profile.settings.incognito.IncognitoSettings;
import me.dmk.core.util.TimeUtil;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.StringUtil;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "incognito")
@Permission("core.command.incognito")
public class IncognitoCommand {

    private final NotificationController notificationController;
    private final ProfileController profileController;
    private final IncognitoController incognitoController;
    private final ProfileCache profileCache;

    @Async
    @Execute(required = 0)
    void execute(Player player) {
        Profile profile = this.profileCache.getOrElseThrow(player.getUniqueId());
        IncognitoSettings incognitoSettings = profile.getProfileSettings().getIncognitoSettings();

        boolean changed = this.incognitoController.changeSkin(player, profile);

        String message;
        if (changed) {
            message = StringFormatter.formatSuccess() + " <gray>Tryb anonimowy został " + StringFormatter.formatBoolean(incognitoSettings.isEnabled()) + "<dark_gray>.";
        } else {
            message = StringFormatter.formatError() + " <red>Wystąpił błąd z API, spróbuj ponownie później.";
        }

        this.notificationController.sendMessage(player,
                message
        );
    }

    @Async
    @Execute(route = "changeIdentifier")
    @Permission("core.command.incognito.changeidentifier")
    void executeChangeIdentifier(Player player) {
        Profile profile = this.profileCache.getOrElseThrow(player.getUniqueId());
        IncognitoSettings incognitoSettings = profile.getProfileSettings().getIncognitoSettings();

        if (!incognitoSettings.canChangeIdentifier()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Ponownie identyfikator będziesz mógł/a zmienić za <gold>" + TimeUtil.instantToString(incognitoSettings.getWhenCanChange(), true) + "<dark_gray>."
            );
            return;
        }

        String newIdentifier = incognitoSettings.changeIdentifier();

        this.profileController.save(profile);

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringUtil.getGreenGradient() + " Zmieniono </gradient><gray>twój identyfikator anonimowego na <light_purple>" + newIdentifier + "<dark_gray>."
        );
    }

    @Async
    @Execute(route = "find", required = 1)
    @Permission("core.command.incognito.find")
    void execute(Player player, @Arg @Name("identifier") String identifier) {
        if (identifier.length() != 8) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Identyfikator trybu anonimowego musi mieć długość 8 znaków<dark_gray>."
            );
            return;
        }

        Optional<Profile> profile = this.incognitoController.findProfileByIdentifier(identifier);
        if (profile.isEmpty()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie znaleziono profilu<dark_gray>."
            );
            return;
        }

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + " <gray>Gracz o numerze identyfikacyjnym animowego <light_purple>" + identifier + " <gray>to <light_purple>" + profile.get().getName() + " <dark_gray>(<light_purple>" + profile.get().getUuid() + "<dark_gray>)."
        );
    }
}

package me.dmk.core.command.implementation.player;

import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.kit.Kit;
import me.dmk.core.kit.KitMap;
import me.dmk.core.kit.gui.KitGui;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.statistics.ProfileStatistics;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

/**
 * Created by DMK on 22.02.2023
 */

@AllArgsConstructor

@Route(name = "kit")
public class KitCommand {

    private final NotificationController notificationController;
    private final ProfileController profileController;
    private final KitMap kitMap;

    @Execute
    void execute(Player player, Profile profile) {
        new KitGui(player, profile).open();
    }

    @Async
    @Execute(route = "upgrade")
    void executeUpgrade(Player player, Profile profile) {
        ProfileStatistics statistics = profile.getProfileStatistics();

        Optional<Kit> nextKitOptional = this.kitMap.get(statistics.getKitLevel() + 1);
        if (nextKitOptional.isEmpty()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie ma następnego zestawu do odblokowania<dark_gray>."
            );
            return;
        }

        Kit nextKit = nextKitOptional.get();

        if (nextKit.getRequiredCoinsToBuy() > statistics.getCoins()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Aby odblokować następny zestaw potrzebujesz <gold>" + nextKit.getRequiredCoinsToBuy() + " <red>monet<dark_gray>."
            );
            return;
        }

        statistics.increaseKitLevel();

        this.profileController.save(profile);

        this.notificationController.sendMessage(player,
                List.of(
                        StringFormatter.formatSuccess() + StringFormatter.formatGreenGradient() + " Zwiększono</gradient> <gray>poziom zestawu<dark_gray>!",
                        StringFormatter.formatWarning() + " <gold>Zestaw będzie dostępny po ponownym wejściu na serwer lub odrodzeniu<dark_gray>."
                )
        );
    }
}

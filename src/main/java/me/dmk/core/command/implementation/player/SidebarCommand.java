package me.dmk.core.command.implementation.player;

import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.profile.settings.board.Board;
import me.dmk.core.util.StyleUtil;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "sidebar")
public class SidebarCommand {

    private final NotificationController notificationController;
    private final ProfileCache profileCache;

    @Async
    @Execute
    void execute(Player player) {
        Profile profile = this.profileCache.getOrElseThrow(player.getUniqueId());
        ProfileSettings profileSettings = profile.getProfileSettings();

        Board board = profileSettings.getBoard();

        if (board.isEnabled()) {
            board.setEnabled(false);
            board.remove();
        } else {
            board.setEnabled(true);
            board.create(player, profile);
        }

        this.notificationController.sendMessage(player,
                StyleUtil.getSuccess() + " <gray>Boczny panel został " + StyleUtil.formatBoolean(board.isEnabled()) + "<dark_gray>."
        );
    }
}

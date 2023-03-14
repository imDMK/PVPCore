package me.dmk.core.profile.settings.board;

import fr.mrmicky.fastboard.FastBoard;
import lombok.AllArgsConstructor;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.settings.ProfileSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by DMK on 01.01.2023
 */

@AllArgsConstructor
public class BoardTask implements Runnable {

    private final ProfileController profileController;

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Optional<Profile> profileOptional = this.profileController.get(player.getUniqueId());
            if (profileOptional.isEmpty()) {
                return;
            }

            Profile profile = profileOptional.get();
            ProfileSettings profileSettings = profile.getProfileSettings();
            Board board = profileSettings.getBoard();

            if (board.isEnabled()) {
                FastBoard fastBoard = board.getFastBoard();
                if (fastBoard == null || fastBoard.isDeleted()) {
                    board.create(player, profile);
                    return;
                }

                board.update(player, profile);
            }
        }
    }
}

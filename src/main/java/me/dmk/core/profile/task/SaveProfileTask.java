package me.dmk.core.profile.task;

import lombok.AllArgsConstructor;
import me.dmk.core.profile.controller.ProfileController;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 05.01.2023
 */


@AllArgsConstructor
public class SaveProfileTask implements Runnable {

    private final ProfileController profileController;

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.profileController.get(player.getUniqueId()).ifPresent(profileController::save);
        }
    }
}

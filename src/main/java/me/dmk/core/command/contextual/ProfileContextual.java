package me.dmk.core.command.contextual;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.contextual.Contextual;
import lombok.AllArgsConstructor;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import panda.std.Result;

/**
 * Created by DMK on 05.03.2023
 */

@AllArgsConstructor
public class ProfileContextual implements Contextual<CommandSender, Profile> {

    private final ProfileController profileController;

    @Override
    public Result<Profile, ?> extract(CommandSender commandSender, Invocation<CommandSender> invocation) {
        if (commandSender instanceof Player player) {
            Profile profile = this.profileController.getOrElseThrow(player);
            return Result.ok(profile);
        }

        return Result.error("<red>Nie możesz użyć tej komendy<dark_gray>.");
    }
}

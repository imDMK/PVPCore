package me.dmk.core.command.implementation.player;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.gui.ProfileManageGui;
import me.dmk.core.profile.gui.ProfilePanelGui;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "profile")
public class ProfileCommand {

    private final ProfileCache profileCache;

    @Execute(required = 0)
    void execute(Player player) {
        Profile profile = this.profileCache.getOrElseThrow(player);

        new ProfilePanelGui(player, profile).open();
    }

    @Execute(required = 1)
    void execute(Player player, @Arg Profile profile) {
        new ProfilePanelGui(player, profile).open();
    }

    @Execute(route = "manage", required = 1)
    @Permission("core.command.profile.manage")
    void executeManage(Player player, @Arg Profile profile) {
        new ProfileManageGui(player, profile).open();
    }
}

package me.dmk.core.command.implementation.player;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.gui.tops.TopsGui;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.profile.controller.ProfileController;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 12.02.2023
 */

@AllArgsConstructor

@Route(name = "tops")
public class TopsCommand {

    private final ProfileController profileController;
    private final GuildController guildController;

    @Execute(required = 0)
    void execute(Player player) {
        new TopsGui(this.profileController, this.guildController).open(player);
    }
}

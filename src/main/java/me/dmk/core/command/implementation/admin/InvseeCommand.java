package me.dmk.core.command.implementation.admin;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 29.12.2022
 */

@Route(name = "invsee")
@Permission("core.command.invsee")
public class InvseeCommand {

    @Execute(required = 1)
    void execute(Player player, @Arg @Name("player") Player other) {
        player.openInventory(other.getInventory());
    }
}

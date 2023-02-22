package me.dmk.core.command.implementation.player;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.gui.tops.TopsGui;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 12.02.2023
 */

@AllArgsConstructor

@Route(name = "tops")
public class TopsCommand {

    @Execute
    void execute(Player player) {
        new TopsGui().open(player);
    }
}

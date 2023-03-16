package me.dmk.core.command.implementation.player;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import me.dmk.core.gui.weather.WeatherGui;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 16.03.2023
 */

@Route(name = "weather")
@Permission("core.command.weather")
public class WeatherCommand {

    @Execute
    void execute(Player player) {
        new WeatherGui(player).open();
    }
}

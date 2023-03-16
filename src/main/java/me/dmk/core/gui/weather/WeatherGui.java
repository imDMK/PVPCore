package me.dmk.core.gui.weather;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.util.ComponentUtil;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 16.03.2023
 */

public class WeatherGui extends PluginGui {

    public WeatherGui(Player player) {
        super(player, "Zmiana pogody", 3, true, true);
    }

    @Override
    public void build() {
        GuiItem dayItem = ItemBuilder.from(Material.YELLOW_CONCRETE)
                .name(ComponentUtil.text("<yellow>Dzień"))
                .asGuiItem(event ->
                        this.player.setPlayerTime(1000, false)
                );

        GuiItem nightItem = ItemBuilder.from(Material.BLACK_CONCRETE)
                .name(ComponentUtil.text("<dark_gray>Noc"))
                .asGuiItem(event ->
                        this.player.setPlayerTime(19000, false)
                );

        GuiItem sunriseItem = ItemBuilder.from(Material.ORANGE_CONCRETE)
                .name(ComponentUtil.text("<gold>Zachód słońca"))
                .asGuiItem(event ->
                        this.player.setPlayerTime(23400, false)
                );

        GuiItem clearWeatherItem = ItemBuilder.from(Material.LIGHT_BLUE_CONCRETE)
                .name(ComponentUtil.text("<aqua>Ładna pogoda"))
                .asGuiItem(event ->
                        this.player.setPlayerWeather(WeatherType.CLEAR)
                );

        GuiItem downfallWeatherItem = ItemBuilder.from(Material.LIGHT_GRAY_CONCRETE)
                .name(ComponentUtil.text("<gray>Deszczowa pogoda"))
                .asGuiItem(event ->
                        this.player.setPlayerWeather(WeatherType.DOWNFALL)
                );

        GuiItem resetToDefaultItem = ItemBuilder.from(Material.LEVER)
                .name(ComponentUtil.text("<gray>Zresetuj do domyślnych"))
                .asGuiItem(event -> {
                    this.player.resetPlayerTime();
                    this.player.resetPlayerWeather();
                });

        this.gui.setItem(11, dayItem);
        this.gui.setItem(12, nightItem);
        this.gui.setItem(13, sunriseItem);
        this.gui.setItem(14, downfallWeatherItem);
        this.gui.setItem(15, clearWeatherItem);
        this.gui.setItem(22, resetToDefaultItem);
    }
}

package me.dmk.core.gui.weather;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.profile.Profile;
import me.dmk.core.util.ComponentUtil;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 16.03.2023
 */

public class WeatherGui extends PluginGui {

    private final Profile profile;

    public WeatherGui(Player player, Profile profile) {
        super(player, "Zmiana pogody", 3, true, true);

        this.profile = profile;
    }

    @Override
    public void build() {
        GuiItem dayItem = ItemBuilder.from(Material.YELLOW_CONCRETE)
                .name(ComponentUtil.text("<yellow>Dzień"))
                .asGuiItem(event -> {
                    this.player.setPlayerTime(1000, false);
                    this.profile.setPlayerTime(1000);
                });

        GuiItem nightItem = ItemBuilder.from(Material.BLACK_CONCRETE)
                .name(ComponentUtil.text("<dark_gray>Noc"))
                .asGuiItem(event -> {
                    this.player.setPlayerTime(19000, false);
                    this.profile.setPlayerTime(19000);
                });

        GuiItem sunriseItem = ItemBuilder.from(Material.ORANGE_CONCRETE)
                .name(ComponentUtil.text("<gold>Zachód słońca"))
                .asGuiItem(event -> {
                    this.player.setPlayerTime(23400, false);
                    this.profile.setPlayerTime(23400);
                });

        GuiItem clearWeatherItem = ItemBuilder.from(Material.LIGHT_BLUE_CONCRETE)
                .name(ComponentUtil.text("<aqua>Ładna pogoda"))
                .asGuiItem(event -> {
                    this.player.setPlayerWeather(WeatherType.CLEAR);
                    this.profile.setWeatherType(WeatherType.CLEAR);
                });

        GuiItem downfallWeatherItem = ItemBuilder.from(Material.LIGHT_GRAY_CONCRETE)
                .name(ComponentUtil.text("<gray>Deszczowa pogoda"))
                .asGuiItem(event -> {
                    this.player.setPlayerWeather(WeatherType.DOWNFALL);
                    this.profile.setWeatherType(WeatherType.DOWNFALL);
                });

        GuiItem resetToDefaultItem = ItemBuilder.from(Material.LEVER)
                .name(ComponentUtil.text("<gray>Zresetuj do domyślnych"))
                .asGuiItem(event -> {
                    this.player.resetPlayerTime();
                    this.player.resetPlayerWeather();

                    this.profile.setPlayerTime(0L);
                    this.profile.setWeatherType(null);
                });

        this.gui.setItem(11, dayItem);
        this.gui.setItem(12, nightItem);
        this.gui.setItem(13, sunriseItem);
        this.gui.setItem(14, downfallWeatherItem);
        this.gui.setItem(15, clearWeatherItem);
        this.gui.setItem(22, resetToDefaultItem);
    }
}

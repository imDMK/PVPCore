package me.dmk.core.profile.settings.nametag;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by DMK on 30.12.2022
 */

@AllArgsConstructor
public enum ColorNameType {
    DEAFULT("<gray>"),
    WHITE("<white>"),
    PURPLE("<light_purple>"),
    AQUA("<aqua>"),
    YELLOW("<yellow>"),
    GOLD("<gold>"),
    GREEN("<green>"),
    RAINBOW("<rainbow>"),
    GOLD_YELLOW_GRADIENT("<gradient:gold:yellow>"),
    GREEN_GRADIENT("<gradient:green:dark_green>"),
    PURPLE_GRADIENT("<gradient:light_purple:dark_purple>"),
    AQUA_GRADIENT("<gradient:aqua:dark_aqua>"),
    BLUE_GRADIENT("<gradient:blue:dark_blue>"),
    GRAY_GRADIENT("<gradient:gray:dark_gray>");

    @Getter
    private final String format;
}

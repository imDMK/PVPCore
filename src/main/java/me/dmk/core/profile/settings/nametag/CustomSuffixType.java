package me.dmk.core.profile.settings.nametag;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by DMK on 12.01.2023
 */

@AllArgsConstructor
public enum CustomSuffixType {
    NONE(""),
    HEART("<red>❤"),
    FLOWER("<yellow>✿"),
    SMILE("<green>☻"),
    UNBRELLA("<white>☂"),
    CRUCIFIX("<red>✞"),
    DEATH("<red>☠"),
    STAR("<gold>✦"),
    CLOUD("<white>☁");

    @Getter
    private final String format;
}

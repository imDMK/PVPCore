package me.dmk.core.profile.settings;

import com.google.common.collect.Sets;
import lombok.Data;
import me.dmk.core.profile.settings.board.Board;
import me.dmk.core.profile.settings.incognito.IncognitoSettings;
import me.dmk.core.profile.settings.nametag.ColorNameType;
import me.dmk.core.profile.settings.nametag.CustomSuffixType;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * Created by DMK on 29.12.2022
 */

@Data
public final class ProfileSettings implements Serializable {

    private Set<UUID> ignoredPlayers = Sets.newConcurrentHashSet();

    private ColorNameType colorName = ColorNameType.DEAFULT;
    private CustomSuffixType customSuffix = CustomSuffixType.NONE;

    private IncognitoSettings incognitoSettings = new IncognitoSettings();
    private Board board = new Board();

    private boolean god = false;
    private boolean vanish = false;
    private boolean sounds = true;

    private boolean privateMessages = true;
    private boolean achievementsMessages = true;
    private boolean deathMessages = true;
    private boolean systemMessages = true;
    private boolean guildMessages = true;
    private boolean globalMessages = true;

    @Nullable private transient UUID lastPrivateMessage = null;
}

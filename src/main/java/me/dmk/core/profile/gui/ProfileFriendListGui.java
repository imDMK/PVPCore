package me.dmk.core.profile.gui;

import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.CorePlugin;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.gui.item.storage.SkullStorage;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.TimeUtil;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by DMK on 13.03.2023
 */


public class ProfileFriendListGui extends PluginGui {

    private final ProfileCache profileCache = CorePlugin.getCorePlugin().getProfileCache();

    private final Profile profile;

    public ProfileFriendListGui(Player player, Profile profile) {
        super(player, "Lista przyjaciół " + profile.getName(), 6, true, true);

        this.profile = profile;
    }

    @Override
    public void build() {
        boolean isSelf = this.player.getUniqueId().equals(this.profile.getUuid());

        GuiItem backButton = this.createBackButton(event -> new ProfilePanelGui(this.player, this.profile).open(),
                "",
                StringFormatter.formatWarning() + " <gold>Kliknij<dark_gray>, <gray>aby powrócić do panelu profilu<dark_gray>.",
                ""
        );

        this.gui.setItem(49, backButton);

        this.profile.getFriends().forEach((uuid, date) -> {
            Optional<Profile> friendProfileOptional = this.profileCache.getOrElseLoad(uuid);
            if (friendProfileOptional.isEmpty()) {
                this.profile.getFriends().remove(uuid);
                return;
            }

            Profile friendProfile = friendProfileOptional.get();
            OfflinePlayer friendPlayer = Bukkit.getOfflinePlayer(uuid);

            List<String> friendItemLore = new ArrayList<>(Arrays.asList(
                    "",
                    this.circle + " <gray>Przyjaźń od <light_purple>" + TimeUtil.formatDate(date) + "<dark_gray>.",
                    ""
            ));

            if (isSelf) {
                friendItemLore.addAll(Arrays.asList(
                        StringFormatter.formatWarning() + " <gold>Kliknij<dark_gray>, <gray>aby <red>usunąć <gray>gracza z listy przyjaciół<dark_gray>.",
                        ""
                ));
            }

            GuiItem friendItem = SkullStorage.createPlayerHead(uuid)
                    .name(ComponentUtil.text("<light_purple>" + friendPlayer.getName()))
                    .lore(ComponentUtil.asList(friendItemLore))
                    .asGuiItem(event -> {
                        if (!isSelf) {
                            return;
                        }

                        this.profile.removeFriend(uuid);
                        friendProfile.removeFriend(this.profile.getUuid());

                        if (this.profile.getFriends().isEmpty()) { //Do not open this gui when the list is empty.
                            new ProfilePanelGui(this.player, this.profile).open();
                        } else {
                            this.open();
                        }
                    });

            this.gui.addItem(friendItem);
        });
    }
}

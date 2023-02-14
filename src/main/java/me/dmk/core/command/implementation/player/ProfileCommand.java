package me.dmk.core.command.implementation.player;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.gui.ProfilePanelGui;
import me.dmk.core.util.StyleUtil;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor

@Route(name = "profile")
public class ProfileCommand {

    private final PluginConfiguration pluginConfiguration;
    private final LuckPermsController luckPermsController;
    private final NotificationController notificationController;
    private final ProfileController profileController;
    private final ProfileCache profileCache;
    private final GuildCache guildCache;

    @Execute(required = 0)
    void execute(Player player) {
        Profile profile = this.profileCache.getOrElseThrow(player.getUniqueId());

        new ProfilePanelGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache)
                .open(player, profile);
    }

    @Execute(required = 1)
    void execute(Player player, @Arg Profile profile) {
        new ProfilePanelGui(this.pluginConfiguration, this.luckPermsController, this.profileController, this.profileCache, this.guildCache)
                .open(player, profile);
    }

    @Async
    @Execute(route = "addCoins", required = 2)
    @Permission("core.command.profile.addcoins")
    void executeAddCoins(Player player, @Arg Profile profile, @Arg Integer coins) {
        profile.getProfileStatistics().addCoins(coins);
        this.profileController.save(profile);

        this.notificationController.sendMessage(player,
                StyleUtil.getSuccess() + " <gray>Dodano <light_purple>" + coins + " <gray>monet do konta gracza <light_purple>" + profile.getName() + "<dark_gray>."
        );
    }

    @Async
    @Execute(route = "removeCoins", required = 2)
    @Permission("core.command.profile.removecoins")
    void executeRemoveCoins(Player player, @Arg Profile profile, @Arg Integer coins) {
        profile.getProfileStatistics().removeCoins(coins);
        this.profileController.save(profile);

        this.notificationController.sendMessage(player,
                StyleUtil.getSuccess() + " <gray>Usunięto <light_purple>" + coins + " <gray>monet z konta gracza <light_purple>" + profile.getName() + "<dark_gray>."
        );
    }

    @Async
    @Execute(route = "setCoins", required = 2)
    @Permission("core.command.profile.setcoins")
    void executeSetCoins(Player player, @Arg Profile profile, @Arg Integer coins) {
        profile.getProfileStatistics().setCoins(coins);
        this.profileController.save(profile);

        this.notificationController.sendMessage(player,
                StyleUtil.getSuccess() + " <gray>Zmieniono monety konta gracza <light_purple>" + profile.getName() + " <gray>na <light_purple>" + coins + "<dark_gray>."
        );
    }
}

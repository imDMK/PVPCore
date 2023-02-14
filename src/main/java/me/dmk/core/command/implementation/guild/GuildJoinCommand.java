package me.dmk.core.command.implementation.guild;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.util.StyleUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 08.01.2023
 */

@AllArgsConstructor

@Route(name = "guild join")
public class GuildJoinCommand {

    private final NotificationController notificationController;
    private final ProfileController profileController;
    private final GuildController guildController;
    private final ProfileCache profileCache;

    @Async
    @Execute(required = 1)
    void execute(Player player, @Arg Guild guild) {
        Profile profile = this.profileCache.getOrElseThrow(player.getUniqueId());

        if (!guild.isInvited(player.getUniqueId())) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Nie otrzymałeś/aś zaproszenia do tej gildii<dark_gray>."
            );
            return;
        }

        if (profile.getGuild().isPresent()) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Posiadasz już gildię<dark_gray>... oszust?"
            );
            return;
        }

        guild.acceptInvite(player.getUniqueId());
        profile.setGuildTag(guild.getTag());

        this.guildController.save(guild);
        this.profileController.save(profile);

        this.notificationController.sendMessage(Bukkit.getOnlinePlayers(),
                StyleUtil.getWarning() + " <gray>Gracz <light_purple>" + player.getName() + " <green>dołączył <gray>do gildii " + StyleUtil.formatGuildTag(guild) + "<dark_gray>."
        );
    }
}

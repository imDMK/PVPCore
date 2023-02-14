package me.dmk.core.command.implementation.guild;

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

import java.util.Optional;

/**
 * Created by DMK on 08.01.2023
 */

@AllArgsConstructor

@Route(name = "guild leave")
public class GuildLeaveCommand {

    private final NotificationController notificationController;
    private final ProfileController profileController;
    private final GuildController guildController;
    private final ProfileCache profileCache;

    @Async
    @Execute
    void execute(Player player) {
        Profile profile = this.profileCache.getOrElseThrow(player.getUniqueId());

        Optional<Guild> guildOptional = profile.getGuild();
        if (guildOptional.isEmpty()) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Nie posiadasz gildii<dark_gray>."
            );
            return;
        }

        final Guild guild = guildOptional.get();

        if (guild.isLeader(player.getUniqueId())) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Jesteś liderem tej gildii<dark_gray>."
            );
            return;
        }

        guild.leave(player.getUniqueId());
        profile.setGuildTag(null);

        this.guildController.save(guild);
        this.profileController.save(profile);

        this.notificationController.sendMessage(Bukkit.getOnlinePlayers(),
                StyleUtil.getWarning() + " <gray>Gracz <light_purple>" + player.getName() + " <red>opuścił <gray>gildię <light_purple>" + StyleUtil.formatGuildTag(guild) + "<dark_gray>."
        );
    }
}

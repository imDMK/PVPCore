package me.dmk.core.command.implementation.guild;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.member.Member;
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

@Route(name = "guild kick")
public class GuildKickCommand {

    private final NotificationController notificationController;
    private final ProfileController profileController;
    private final GuildController guildController;
    private final ProfileCache profileCache;

    @Async
    @Execute(required = 1)
    void execute(Player player, @Arg Member member) {
        Profile profile = this.profileCache.getOrElseThrow(player.getUniqueId());

        Optional<Guild> guildOptional = profile.getGuild();

        if (guildOptional.isEmpty()) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Nie posiadasz gildii<dark_gray>."
            );
            return;
        }

        final Guild guild = guildOptional.get();

        if (!guild.isLeaderOrCoLeader(player.getUniqueId())) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Nie posiadasz gildyjnych uprawnień<dark_gray>."
            );
            return;
        }

        if (!guild.isMember(member.getUuid())) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Ten gracz nie jest w twojej gildii<dark_gray>."
            );
            return;
        }

        if (player.getUniqueId().equals(member.getUuid())) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Zwariowałeś? Nie możesz wyrzucić samego siebie z giildii<dark_gray>..."
            );
            return;
        }

        Optional<Profile> memberProfileOptional = this.profileCache.getOrElseLoad(member.getUuid());
        if (memberProfileOptional.isEmpty()) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Wystąpił błąd<dark_gray>."
            );
            return;
        }

        Profile memberProfile = memberProfileOptional.get();

        memberProfile.setGuildTag(null);
        guild.leave(member.getUuid());

        this.guildController.save(guild);
        this.profileController.save(memberProfile);

        this.notificationController.sendMessage(Bukkit.getOnlinePlayers(),
                StyleUtil.getWarning() + " <gray>Gracz <light_purple>" + memberProfile.getName() + " <gray>został <red>wyrzucony <gray>z gildii " + StyleUtil.formatGuildTag(guild) + "<dark_gray>."
        );
    }
}

package me.dmk.core.command.implementation.guild;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.chat.notification.PluginMessageType;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.guild.member.GuildMember;
import me.dmk.core.guild.rank.GuildRank;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.util.string.StringFormatter;
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
    void execute(Player player, Guild guild, @Arg GuildMember guildMember) {
        GuildRank guildRank = guild.getGuildRank(player.getUniqueId());

        if (!guild.isLeader(player.getUniqueId()) || !guildRank.isCanManageMembers()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie posiadasz gildyjnych uprawnień<dark_gray>."
            );
            return;
        }

        if (!guild.isMember(guildMember.getUuid())) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Ten gracz nie jest członkiem twojej gildii<dark_gray>."
            );
            return;
        }

        if (player.getUniqueId().equals(guildMember.getUuid())) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Zwariowałeś? Nie możesz wyrzucić samego siebie z giildii<dark_gray>..."
            );
            return;
        }

        Optional<Profile> memberProfileOptional = this.profileCache.getOrElseLoad(guildMember.getUuid());
        if (memberProfileOptional.isEmpty()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Wyrzucono gracza<dark_gray>."
            );

            guild.leaveMembership(guildMember.getUuid());
            return;
        }

        Profile memberProfile = memberProfileOptional.get();

        guild.leaveMembership(guildMember.getUuid());

        this.guildController.save(guild);
        this.profileController.save(memberProfile);

        this.notificationController.sendGlobalPluginMessage(
                PluginMessageType.GUILD,
                StringFormatter.formatWarning() + " <gray>Gracz <light_purple>" + memberProfile.getName() + " <gray>został <red>wyrzucony <gray>z gildii <light_purple>" + guild.getTag() + "<dark_gray>."
        );
    }
}

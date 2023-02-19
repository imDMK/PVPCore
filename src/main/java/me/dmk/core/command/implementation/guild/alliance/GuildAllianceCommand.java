package me.dmk.core.command.implementation.guild.alliance;

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
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by DMK on 04.02.2023
 */

@AllArgsConstructor

@Route(name = "guild alliance")
public class GuildAllianceCommand {

    private final NotificationController notificationController;
    private final GuildController guildController;
    private final ProfileCache profileCache;

    @Async
    @Execute(required = 1)
    @Route(name = "accept")
    void executeAccept(Player player, @Arg Guild otherGuild) {
        Profile profile = this.profileCache.getOrElseThrow(player.getUniqueId());

        Optional<Guild> guildOptional = profile.getGuild();
        if (guildOptional.isEmpty()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie posiadasz gildii<dark_gray>."
            );
            return;
        }

        final Guild guild = guildOptional.get();

        if (!otherGuild.isInvitedToAlliance(guild)) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie otrzymano zaproszenia do sojuszu od tej gildii<dark_gray>."
            );
            return;
        }

        guild.acceptAllianceInvite(otherGuild);
        otherGuild.acceptAllianceInvite(guild);

        this.guildController.save(guild);
        this.guildController.save(otherGuild);

        this.notificationController.sendMessage(Bukkit.getOnlinePlayers(),
                StringFormatter.formatWarning() + " <gray>Gildia <light_purple>" + guild.getTag() + " <green>zawarła <gray>sojusz <gray>z gildią <light_purple>" + otherGuild.getTag() + "<dark_gray>."
        );
    }

    @Async
    @Execute(required = 1)
    @Route(name = "break")
    void executeBreak(Player player, @Arg Guild otherGuild) {
        Profile profile = this.profileCache.getOrElseThrow(player.getUniqueId());

        Optional<Guild> guildOptional = profile.getGuild();
        if (guildOptional.isEmpty()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie posiadasz gildii<dark_gray>."
            );
            return;
        }

        final Guild guild = guildOptional.get();

        if (!guild.hasAlliance(otherGuild)) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie posiadacie sojuszu gildyjnego<dark_gray>."
            );
            return;
        }

        guild.breakAlliance(otherGuild);
        otherGuild.breakAlliance(guild);

        this.guildController.save(guild);
        this.guildController.save(otherGuild);

        this.notificationController.sendMessage(Bukkit.getOnlinePlayers(),
                StringFormatter.formatWarning() + " <gray>Gildia <light_purple>" + guild.getTag() + " <red>zerwała <gray>sojusz z gildią <light_purple>" + otherGuild.getTag() + "<dark_gray>."
        );
    }

    @Async
    @Execute(required = 1)
    @Route(name = "invite")
    void executeInvite(Player player, @Arg Guild otherGuild) {
        Profile profile = this.profileCache.getOrElseThrow(player.getUniqueId());

        Optional<Guild> guildOptional = profile.getGuild();

        if (guildOptional.isEmpty()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie posiadasz gildii<dark_gray>."
            );
            return;
        }

        final Guild guild = guildOptional.get();

        if (guild.getTag().equals(otherGuild.getTag())) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Zwariowałeś? Nie możesz zaprosić swojej gildii do sojuszu <dark_gray>damn..."
            );
            return;
        }

        if (!guild.isLeaderOrCoLeader(player.getUniqueId())) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie posiadasz gildyjnych uprawnień<dark_gray>."
            );
            return;
        }

        if (otherGuild.hasAlliance(guild)) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Posiadacie już sojusz gildyjny<dark_gray>, <red>aby go zerwać użyj<dark_gray>: <gold>/guild alliance break " + otherGuild.getTag() + "<dark_gray>."
            );
            return;
        }

        if (otherGuild.isInvitedToAlliance(guild)) {
            Bukkit.getServer().dispatchCommand(player, "guild alliance accept " + otherGuild.getTag());
            return;
        }

        if (guild.isInvitedToAlliance(otherGuild)) {
            guild.cancelInviteToAlliance(otherGuild);

            this.notificationController.sendMessage(otherGuild,
                    StringFormatter.formatGuild() + " <gray>Gildia <light_purple>" + otherGuild.getTag() + " <red>anulowała <gray>zaproszenie do sojuszu<dark_gray>."
            );

            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Anulowano <gray>zaproszenie do sojuszu gildii <light_purple>" + guild.getTag() + "<dark_gray>."
            );
            return;
        }

        guild.inviteToAlliance(otherGuild);

        this.notificationController.sendMessage(otherGuild,
                StringFormatter.formatGuild() + " <gray>Otrzymaliście zaproszenie sojuszu od gildii <light_purple>" + guild.getTag() + "<dark_gray>."
        );

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + StringUtil.getGreenGradient() + " Zaproszono <gray>gildię <light_purple>" + otherGuild.getTag() + " <gray>do sojuszu<dark_gray>."
        );
    }
}

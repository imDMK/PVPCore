package me.dmk.core.command.implementation.guild.alliance;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.chat.notification.PluginMessageType;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.guild.rank.GuildRank;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 04.02.2023
 */

@AllArgsConstructor

@Route(name = "guild alliance")
public class GuildAllianceCommand {

    private final NotificationController notificationController;
    private final GuildController guildController;

    @Async
    @Execute(required = 1)
    @Route(name = "accept")
    void executeAccept(Player player, Guild guild, @Arg Guild otherGuild) {
        GuildRank guildRank = guild.getGuildRank(player.getUniqueId());

        if (!guild.isLeader(player.getUniqueId()) || !guildRank.isCanManageAlliances()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie posiadasz gildyjnych uprawnień<dark_gray>."
            );
            return;
        }

        if (!otherGuild.isInvitedToAlliance(guild)) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie otrzymano zaproszenia do sojuszu od tej gildii<dark_gray>."
            );
            return;
        }

        guild.acceptInviteToAlliance(otherGuild);
        otherGuild.acceptInviteToAlliance(guild);

        this.guildController.save(guild);
        this.guildController.save(otherGuild);

        this.notificationController.sendGlobalPluginMessage(
                PluginMessageType.GUILD,
                StringFormatter.formatWarning() + " <gray>Gildia <light_purple>" + guild.getTag() + " <green>zawarła <gray>sojusz <gray>z gildią <light_purple>" + otherGuild.getTag() + "<dark_gray>."
        );
    }

    @Async
    @Execute(required = 1)
    @Route(name = "break")
    void executeBreak(Player player, Guild guild, @Arg Guild otherGuild) {
        GuildRank guildRank = guild.getGuildRank(player.getUniqueId());

        if (!guild.isLeader(player.getUniqueId()) || !guildRank.isCanManageAlliances()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie posiadasz gildyjnych uprawnień<dark_gray>."
            );
            return;
        }

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

        this.notificationController.sendGlobalPluginMessage(
                PluginMessageType.GUILD,
                StringFormatter.formatWarning() + " <gray>Gildia <light_purple>" + guild.getTag() + " <red>zerwała <gray>sojusz z gildią <light_purple>" + otherGuild.getTag() + "<dark_gray>."
        );
    }

    @Execute(required = 1)
    @Route(name = "invite")
    void executeInvite(Player player, Guild guild, @Arg Guild otherGuild) {
        GuildRank guildRank = guild.getGuildRank(player.getUniqueId());

        if (!guild.isLeader(player.getUniqueId()) || !guildRank.isCanManageAlliances()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie posiadasz gildyjnych uprawnień<dark_gray>."
            );
            return;
        }

        if (guild.getTag().equals(otherGuild.getTag())) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Zwariowałeś? Nie możesz zaprosić swojej gildii do sojuszu <dark_gray>damn..."
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
                StringFormatter.formatSuccess() + StringFormatter.formatGreenGradient() + " Zaproszono <gray>gildię <light_purple>" + otherGuild.getTag() + " <gray>do sojuszu<dark_gray>."
        );
    }
}

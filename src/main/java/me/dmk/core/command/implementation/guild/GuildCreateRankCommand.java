package me.dmk.core.command.implementation.guild;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.guild.rank.GuildRank;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 11.03.2023
 */

@AllArgsConstructor

@Route(name = "guild createrank")
public class GuildCreateRankCommand {

    private final NotificationController notificationController;
    private final GuildController guildController;

    @Async
    @Execute(required = 3)
    void execute(Player player, Guild guild, @Arg @Name("name") String name, @Arg @Name("priority") int priority, @Arg @Name("icon") Material icon) {
        if (!guild.isLeader(player.getUniqueId()) || !guild.getGuildRank(player.getUniqueId()).isCanManageRanks()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie posiadasz uprawnień gildyjnych<dark_gray>."
            );
            return;
        }

        if (name.length() > 10) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nazwa nie może przekraczać długość 10 znaków<dark_gray>."
            );
            return;
        }

        if (priority < 0) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Wprowadzono nieprawidłową ilość priorytetu<dark_gray>."
            );
            return;
        }

        GuildRank guildRank = new GuildRank(name, priority, icon, false);
        guild.getGuildRanks().put(guildRank.getUuid(), guildRank);

        this.guildController.save(guild);

        this.notificationController.sendMessage(player,
                StringFormatter.formatSuccess() + " <green>Dodano <gray>nową rangę <light_purple>" + guildRank.getName() + "<dark_gray>."
        );
    }
}

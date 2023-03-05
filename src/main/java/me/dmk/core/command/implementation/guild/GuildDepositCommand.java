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
import me.dmk.core.guild.treasury.GuildTreasury;
import me.dmk.core.guild.treasury.payment.GuildPayment;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.statistics.ProfileStatistics;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 31.01.2023
 */

@AllArgsConstructor

@Route(name = "guild deposit")
public class GuildDepositCommand {

    private final NotificationController notificationController;
    private final ProfileController profileController;
    private final GuildController guildController;

    @Async
    @Execute(required = 1)
    void execute(Player player, Profile profile, Guild guild, @Arg Integer coins) {
        ProfileStatistics statistics = profile.getProfileStatistics();
        GuildTreasury guildTreasury = guild.getGuildTreasury();

        if (coins > statistics.getCoins()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Nie posiadasz tyle monet<dark_gray>."
            );
            return;
        }

        GuildPayment guildPayment = new GuildPayment(player.getName(), player.getUniqueId(), coins, guildTreasury.getCoins() + coins);

        profile.getProfileStatistics().removeCoins(coins);
        guild.getMembers().get(player.getUniqueId()).addCoinsToTreasury(coins);

        guildTreasury.addPayment(guildPayment);

        this.guildController.save(guild);
        this.profileController.save(profile);

        this.notificationController.sendGlobalPluginMessage(
                PluginMessageType.GUILD,
                StringFormatter.formatWarning() + " <light_purple>" + player.getName() + " <gray>wpłacił <light_purple>" + coins + " <gray>monet do skarbca gildyjnego<dark_gray>."
        );
    }
}

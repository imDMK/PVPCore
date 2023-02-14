package me.dmk.core.command.implementation.guild;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.guild.treasury.GuildTreasury;
import me.dmk.core.guild.treasury.payment.GuildPayment;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.statistics.ProfileStatistics;
import me.dmk.core.util.StyleUtil;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by DMK on 31.01.2023
 */

@AllArgsConstructor

@Route(name = "guild deposit")
public class GuildDepositCommand {

    private final NotificationController notificationController;
    private final ProfileController profileController;
    private final GuildController guildController;
    private final ProfileCache profileCache;

    @Async
    @Execute(required = 1)
    void execute(Player player, @Arg Integer coins) {
        Profile profile = this.profileCache.getOrElseThrow(player.getUniqueId());
        ProfileStatistics statistics = profile.getProfileStatistics();

        Optional<Guild> guildOptional = profile.getGuild();
        if (guildOptional.isEmpty()) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Nie posiadasz gildii<dark_gray>."
            );
            return;
        }

        Guild guild = guildOptional.get();
        GuildTreasury guildTreasury = guild.getGuildTreasury();

        if (coins > statistics.getCoins()) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Nie posiadasz tyle monet<dark_gray>."
            );
            return;
        }

        GuildPayment guildPayment = new GuildPayment(player.getName(), player.getUniqueId(), coins, guildTreasury.getCoins() + coins);

        profile.getProfileStatistics().removeCoins(coins);
        guild.getMembers().get(player.getUniqueId()).addCoinsToTreasury(coins);

        guildTreasury.addPayment(guildPayment);

        this.guildController.save(guild);
        this.profileController.save(profile);

        this.notificationController.sendMessage(guild,
                StyleUtil.getGuild() + " <gray>Członek <light_purple>" + player.getName() + " <gray>wpłacił <light_purple>" + coins + " <gray>monet do skarbca gildyjnego<dark_gray>."
        );
    }
}

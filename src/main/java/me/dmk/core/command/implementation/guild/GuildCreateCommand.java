package me.dmk.core.command.implementation.guild;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.util.StyleUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by DMK on 08.01.2023
 */

@AllArgsConstructor

@Route(name = "guild create")
public class GuildCreateCommand {

    private final PluginConfiguration pluginConfiguration;
    private final NotificationController notificationController;
    private final GuildController guildController;
    private final ProfileCache profileCache;
    private final GuildCache guildCache;

    @Async
    @Execute(required = 2)
    void execute(Player player, @Arg @Name("tag") String tag, @Arg @Name("name") String name) {
        Profile profile = this.profileCache.getOrElseThrow(player.getUniqueId());

        if (profile.getGuild().isPresent()) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Posiadasz już gildię<dark_gray>."
            );
            return;
        }

        int requiredCoins = this.pluginConfiguration.getCoinsToCreateGuild();
        int requiredLevel = this.pluginConfiguration.getLevelToCreateGuild();

        if (profile.getProfileStatistics().getCoins() < requiredCoins || player.getLevel() < requiredLevel) {
            this.notificationController.sendMessage(player, List.of(
                    StyleUtil.getError() + " <red>Wymagania, aby szałożyć gildię<dark_gray>:",
                    "<dark_gray>- <gold>" + requiredCoins + " <red>monet<dark_gray>,",
                    "<dark_gray>- <gold>" + requiredLevel + " <red>poziom doświadczenia<dark_gray>."
            ));
            return;
        }

        if (name.length() < 4 || name.length() > 20) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Długość nazwy gildii musi być od 4 do 20 znaków<dark_gray>."
            );
            return;
        }

        if (tag.length() > 4 || tag.length() < 3) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Długość tagu gildii musi wynosić od 3 do 4 znaków<dark_gray>."
            );
            return;
        }

        if (this.guildCache.getOrElseLoad(tag).isPresent()) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Gildia o podanym tagu już istnieje<dark_gray>, <red>wymyśl coś innego<dark_gray>..."
            );
            return;
        }

        if (requiredCoins > 0) {
            profile.getProfileStatistics().setCoins(profile.getProfileStatistics().getCoins() - requiredCoins);
        }

        Guild guild = new Guild(tag, name, player.getUniqueId());

        profile.setGuildTag(guild.getTag());

        this.guildController.create(guild);
        this.guildCache.add(guild);

        this.notificationController.sendMessage(Bukkit.getOnlinePlayers(),
                StyleUtil.getWarning() + " <gray>Gracz <light_purple>" + player.getName() + " <gray>założył gildię o nazwie <light_purple>" + name + " <gray>oraz tagu <light_purple>" + tag.toUpperCase() + "<dark_gray>," + StyleUtil.getGreenGradient() + " Gratulacje!"
        );
    }
}

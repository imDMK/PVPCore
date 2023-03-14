package me.dmk.core.command.contextual;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.contextual.Contextual;
import me.dmk.core.guild.Guild;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.util.string.StringFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import panda.std.Result;

import java.util.Optional;

/**
 * Created by DMK on 05.03.2023
 */

public class GuildContextual implements Contextual<CommandSender, Guild> {

    private final ProfileController profileController;
    private final Component noGuild;

    public GuildContextual(ProfileController profileController, MiniMessage miniMessage) {
        this.profileController = profileController;

        this.noGuild = miniMessage.deserialize(StringFormatter.formatError() + " <red>Nie posiadasz gildii<dark_gray>.");
    }

    @Override
    public Result<Guild, ?> extract(CommandSender commandSender, Invocation<CommandSender> invocation) {
        if (commandSender instanceof Player player) {
            Profile profile = this.profileController.getOrElseThrow(player);

            Optional<Guild> guild = profile.getGuild();
            if (guild.isPresent()) {
                return Result.ok(guild.get());
            }

            return Result.error(this.noGuild);
        }

        return Result.error("<red>Nie możesz użyć tej komendy<dark_gray>.");
    }
}

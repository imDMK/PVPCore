package me.dmk.core.command.argument.guild;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.util.StyleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import panda.std.Result;

import java.util.List;
import java.util.Optional;

/**
 * Created by DMK on 07.01.2023
 */

@ArgumentName("guildTag")
public class GuildArgument implements OneArgument<Guild> {

    private final GuildCache guildCache;
    private final Component noGuildFound;

    public GuildArgument(GuildCache guildCache, MiniMessage miniMessage) {
        this.guildCache = guildCache;

        this.noGuildFound = miniMessage.deserialize(StyleUtil.getError() + " <red>Nie znaleziono gildii o podanej nazwie<dark_gray>.");
    }

    @Override
    public Result<Guild, ?> parse(LiteInvocation liteInvocation, String argument) {
        Optional<Guild> guild = this.guildCache.getOrElseLoad(argument);
        if (guild.isPresent()) {
            return Result.ok(guild.get());
        }

        return Result.error(this.noGuildFound);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return Suggestion.of(
                this.guildCache.getStringGuildCache().asMap().keySet()
        );
    }
}

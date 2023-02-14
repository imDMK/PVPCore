package me.dmk.core.command.argument.profile;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.util.StyleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import panda.std.Result;

import java.util.List;
import java.util.Optional;

/**
 * Created by DMK on 29.12.2022
 */

@ArgumentName("profile")
public class ProfileArgument implements OneArgument<Profile> {

    private final ProfileCache profileCache;
    private final Component profileNotFound;

    public ProfileArgument(ProfileCache profileCache, MiniMessage miniMessage) {
        this.profileCache = profileCache;
        this.profileNotFound = miniMessage.deserialize(StyleUtil.getError() + " <red>Nie znaleziono profilu o podanej nazwie<dark_gray>.");
    }

    @Override
    public Result<Profile, ?> parse(LiteInvocation liteInvocation, String argument) {
        Optional<Profile> profileOptional = this.profileCache.getOrElseLoad(argument);
        if (profileOptional.isPresent()) {
            return Result.ok(profileOptional.get());
        }

        return Result.error(this.profileNotFound);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return Suggestion.of(
                this.profileCache.getStringProfileCache().asMap().keySet()
        );
    }
}

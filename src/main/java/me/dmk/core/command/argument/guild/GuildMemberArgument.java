package me.dmk.core.command.argument.guild;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.member.GuildMember;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.util.string.StringFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import panda.std.Result;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by DMK on 01.02.2023
 */

@ArgumentName("guildMember")
public class GuildMemberArgument implements OneArgument<GuildMember> {

    private final ProfileController profileController;

    private final Component noGuildError;
    private final Component profileNoPresent;
    private final Component noMemberPresent;

    public GuildMemberArgument(ProfileController profileController, MiniMessage miniMessage) {
        this.profileController = profileController;

        this.noGuildError = miniMessage.deserialize(StringFormatter.formatError() + " <red>Nie posiadasz gildii<dark_gray>.");
        this.profileNoPresent = miniMessage.deserialize(StringFormatter.formatError() + " <red>Nie znaleziono profilu o podanej nazwie<dark_gray>.");
        this.noMemberPresent = miniMessage.deserialize(StringFormatter.formatError() + " <red>Gracz nie jest członkiem w twojej gildii<dark_gray>.");
    }

    @Override
    public Result<GuildMember, ?> parse(LiteInvocation liteInvocation, String argument) {
        if (!(liteInvocation.sender().getHandle() instanceof Player player)) {
            return Result.error("&cNie możesz użyć tej komendy&8.");
        }

        Profile profile = this.profileController.getOrElseThrow(player);

        Optional<Guild> guildOptional = profile.getGuild();
        if (guildOptional.isEmpty()) {
            return Result.error(this.noGuildError);
        }

        Guild guild = guildOptional.get();

        Optional<Profile> otherProfileOptional = this.profileController.getOrElseLoad(argument);
        if (otherProfileOptional.isEmpty()) {
            return Result.error(this.profileNoPresent);
        }

        Profile otherProfile = otherProfileOptional.get();

        if (guild.isMember(otherProfile.getUuid())) {
            return Result.ok(guild.getMembers().get(otherProfile.getUuid()));
        }

        return Result.error(this.noMemberPresent);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        if (!(invocation.sender().getHandle() instanceof Player player)) {
            return Collections.emptyList();
        }

        Profile profile = this.profileController.getOrElseThrow(player);

        Optional<Guild> guildOptional = profile.getGuild();
        if (guildOptional.isEmpty()) {
            return Collections.emptyList();
        }

        Guild guild = guildOptional.get();

        return guild.getMembers().keySet().stream()
                .map(Bukkit::getOfflinePlayer)
                .map(OfflinePlayer::getName)
                .map(Suggestion::of)
                .collect(Collectors.toList());
    }
}

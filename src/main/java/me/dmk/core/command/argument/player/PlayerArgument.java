package me.dmk.core.command.argument.player;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import me.dmk.core.util.StyleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import panda.std.Result;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by DMK on 16.01.2023
 */

@ArgumentName("player")
public class PlayerArgument implements OneArgument<Player> {

    private final Component playerNotFound;

    public PlayerArgument(MiniMessage miniMessage) {
        this.playerNotFound = miniMessage.deserialize(StyleUtil.getError() + " <red>Podany gracz nie istnieje<dark_gray>.");
    }

    @Override
    public Result<Player, ?> parse(LiteInvocation liteInvocation, String argument) {
        Optional<Player> playerOptional = Optional.ofNullable(Bukkit.getServer().getPlayerExact(argument));
        if (playerOptional.isEmpty()) {
            return Result.error(this.playerNotFound);
        }

        return Result.ok(playerOptional.get());
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        Player player = (Player) invocation.sender().getHandle();
        return Bukkit.getServer().getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .filter(name -> !name.contains(player.getName()))
                .map(Suggestion::of)
                .collect(Collectors.toList());
    }
}

package me.dmk.core.command.implementation.guild;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.gui.GuildPanelGui;
import me.dmk.core.profile.Profile;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 08.01.2023
 */
@Route(name = "guild panel")
public class GuildPanelCommand {

    @Execute(required = 0)
    void execute(Player player, Profile profile, Guild guild) {
        new GuildPanelGui(player, profile, guild).open();
    }

    @Execute(required = 1)
    void executeOtherGuild(Player player, Profile profile, @Arg Guild guild) {
        new GuildPanelGui(player, profile, guild).open();
    }
}

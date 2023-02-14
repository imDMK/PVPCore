package me.dmk.core.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by DMK on 28.12.2022
 */

@Header("#")
@Header("# Configuration file for minecraft core plugin")
@Header("#")
public class PluginConfiguration extends OkaeriConfig {

    @Getter
    @Comment("# Default points what players start")
    public int defaultPoints = 1000;

    @Getter
    @Comment("# Time in seconds for the player's fight to go on")
    public int fightTime = 20;

    @Getter
    @Comment("# BossBar name when player has fight")
    public String fightBossBarName = "<gradient:red:dark_red>Jesteś w walce jeszcze <seconds> sekund";

    @Getter
    @Comment("# List of commands allowed when player has fight")
    public List<String> fightBlockedCommands = List.of("/spawn", "/schowek", "/incognito");

    @Getter
    @Comment("# Required level to create a guild (0 = no requirement)")
    public int levelToCreateGuild = 0;

    @Getter
    @Comment("# Required coins to reset statistics (0 = no requirement)")
    public int coinsToResetStatistics = 0;

    @Getter
    @Comment("# Required coins to create a guild (0 = no requirement)")
    public int coinsToCreateGuild = 0;

    @Getter
    @Comment("# Required coins to extend a guild (0 = no requirement)")
    public int coinsToExtendGuild = 1500;

    @Getter
    @Comment("# Time in seconds at which the player can change the incognito ID")
    public long timeToResetIdentifier = 20;

    @Getter
    @Comment("# Sidebar (Legacy formatting &)")
    public String sidebarName = "&d&lPVP&f&lCORE.PL";

    @Getter
    @Comment("# Sidebar lines (Legacy formatting &)")
    public List<String> sidebarList = Arrays.asList(
            "&a",
            "&7Ranga&8: &d<rank>",
            "&7Monety&8: &d<coins>",
            "&7Ping&8: &d<ping>",
            "&b",
            "&7Gildia&8: &d<guild>",
            "&7Punkty&8: &d<points>",
            "&7Zabójstwa&8: &d<kills>",
            "&7Śmierci&8: &d<deaths>",
            "&7KillStreak&8: &d<killstreak>",
            "&7KDR&8: &d<kdr>",
            "&c"
    );

    @Getter
    @Comment("# Welcome message when player join to server")
    public List<String> welcomeMessage = Arrays.asList(
            " ",
            "<green>Witaj na PVPCORE!",
            " "
    );

    public DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();
}

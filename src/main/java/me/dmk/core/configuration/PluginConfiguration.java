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

@Getter

@Header("#")
@Header("# Configuration file for minecraft core plugin")
@Header("#")
public class PluginConfiguration extends OkaeriConfig {

    @Comment("# Default points what players start")
    public int defaultPoints = 1000;

    @Comment("# Time in seconds for the player's fight to go on")
    public int fightTime = 20;

    @Comment("# BossBar name when player has fight")
    public String fightBossBarName = "<gradient:red:dark_red>Jesteś w walce jeszcze <seconds> sekund";

    @Comment("# List of commands allowed when player has fight")
    public List<String> fightBlockedCommands = List.of("/spawn", "/schowek", "/incognito");

    @Comment("# Required level to create a guild (0 = no requirement)")
    public int levelToCreateGuild = 0;

    @Comment("# Required coins to reset statistics (0 = no requirement)")
    public int coinsToResetStatistics = 0;

    @Comment("# Required coins to create a guild (0 = no requirement)")
    public int coinsToCreateGuild = 0;

    @Comment("# Required coins to extend a guild (0 = no requirement)")
    public int coinsToExtendGuild = 1500;

    @Comment("# Time in seconds at which the player can change the incognito ID")
    public long timeToResetIdentifier = 20;

    @Comment("# Sidebar name")
    public String sidebarName = "<rainbow>PVPCORE.PL";

    @Comment("# Sidebar lines")
    public List<String> sidebarLines = Arrays.asList(
            " ",
            "<gray>Ranga<dark_gray>: <light_purple><group>",
            "<gray>Monety<dark_gray>: <light_purple><coins>",
            "<gray>Ping<dark_gray>: <light_purple><ping>",
            " ",
            "<gray>Gildia<dark_gray>: <light_purple><guild>",
            "<gray>Punkty<dark_gray>: <light_purple><points>",
            "<gray>Zabójstwa<dark_gray>: <light_purple><kills>",
            "<gray>Śmierci<dark_gray>: <light_purple><deaths>",
            "<gray>KillStreak<dark_gray>: <light_purple><killstreak>",
            "<gray>KDR<dark_gray>: <light_purple><kdr>",
            " "
    );

    @Comment("# Welcome message when player join to server")
    public List<String> welcomeMessage = Arrays.asList(
            " ",
            "<green>Witaj na PVPCORE!",
            " "
    );

    public DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();
    public KitConfiguration kitConfiguration = new KitConfiguration();
    public MotdConfiguration motdConfiguration = new MotdConfiguration();
}

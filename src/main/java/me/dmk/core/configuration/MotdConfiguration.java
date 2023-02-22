package me.dmk.core.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class MotdConfiguration extends OkaeriConfig {

    @Comment("# Motd line, use {NL} to create a new line.")
    public String motdLine = "<rainbow:!2>PVPCORE -> Twój server PVP</rainbow><red>!";

    @Comment("# Fake players visible after hovering over the number of players. Leave blank to display player list (legacy formatting, no support for HEX).")
    public List<String> fakePlayers = Arrays.asList("&b", "&aThank you for being on your server list &c<3", "&a");

    @Comment("# Number showing the active amount of players (leave -1 to disable)")
    public int activePlayers = -1;

    @Comment("# Number showing the max amount of players")
    public int maxPlayers = 1000;
}

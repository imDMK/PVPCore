package me.dmk.core.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import lombok.Getter;

/**
 * Created by DMK on 28.12.2022
 */

@Getter
public class DatabaseConfiguration extends OkaeriConfig {

    @Comment("# Boolean value whether to connect to the database user.")
    public boolean authentication = false;

    @Comment("# Username")
    public String userName = "";

    @Comment("# Password")
    public String password = "";

    @Comment("# Hostname (example: localhost)")
    public String hostName = "localhost";

    @Comment("# Port (default: 27017")
    public int port = 27017;

    @Comment("# Database name")
    public String databaseName = "pvpcore";
}

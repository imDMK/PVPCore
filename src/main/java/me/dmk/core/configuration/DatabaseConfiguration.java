package me.dmk.core.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import lombok.Getter;

/**
 * Created by DMK on 28.12.2022
 */

@Header("#")
@Header("# MongoDB Database server config")
@Header("#")
public class DatabaseConfiguration extends OkaeriConfig {

    @Getter
    @Comment("# Boolean value whether to connect to the database user.")
    public boolean authentication = false;

    @Getter
    @Comment("# Username")
    public String userName = "";

    @Getter
    @Comment("# Password")
    public String password = "";

    @Getter
    @Comment("# Hostname (example: localhost)")
    public String hostName = "localhost";

    @Getter
    @Comment("# Port (default: 27017")
    public int port = 27017;

    @Getter
    @Comment("# Database name")
    public String databaseName = "pvpcore";

}

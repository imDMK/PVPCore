package me.dmk.core.gui;

import me.dmk.core.CorePlugin;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.gui.item.storage.ItemStorage;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.kit.KitMap;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;

/**
 * Created by DMK on 21.02.2023
 */

public class PluginGui extends ItemStorage {

    public final PluginConfiguration pluginConfiguration = CorePlugin.getCorePlugin().getPluginConfiguration();
    public final LuckPermsController luckPermsController = CorePlugin.getCorePlugin().getLuckPermsController();
    public final ProfileController profileController = CorePlugin.getCorePlugin().getProfileController();
    public final GuildController guildController = CorePlugin.getCorePlugin().getGuildController();
    public final ProfileCache profileCache = CorePlugin.getCorePlugin().getProfileCache();
    public final GuildCache guildCache = CorePlugin.getCorePlugin().getGuildCache();
    public final KitMap kitMap = CorePlugin.getCorePlugin().getKitMap();
}

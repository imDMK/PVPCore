package me.dmk.core;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.adventure.platform.LiteBukkitAdventurePlatformFactory;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import lombok.Getter;
import me.dmk.core.chat.GlobalChatCache;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.chat.notification.NotificationType;
import me.dmk.core.chat.waiter.ChatWaiterCache;
import me.dmk.core.command.argument.guild.GuildArgument;
import me.dmk.core.command.argument.guild.GuildMemberArgument;
import me.dmk.core.command.argument.notification.NotificationTypeArgument;
import me.dmk.core.command.argument.player.GameModeArgument;
import me.dmk.core.command.argument.player.InstantArgument;
import me.dmk.core.command.argument.player.IntegerArgument;
import me.dmk.core.command.argument.player.LocationArgument;
import me.dmk.core.command.argument.player.PlayerArgument;
import me.dmk.core.command.argument.profile.ProfileArgument;
import me.dmk.core.command.contextual.GuildContextual;
import me.dmk.core.command.contextual.ProfileContextual;
import me.dmk.core.command.handler.InvalidUsageHandler;
import me.dmk.core.command.handler.MissingPermissionHandler;
import me.dmk.core.command.implementation.admin.BroadCastCommand;
import me.dmk.core.command.implementation.admin.ChatCommand;
import me.dmk.core.command.implementation.admin.ClearCommand;
import me.dmk.core.command.implementation.admin.FlyCommand;
import me.dmk.core.command.implementation.admin.GameModeCommand;
import me.dmk.core.command.implementation.admin.GodModeCommand;
import me.dmk.core.command.implementation.admin.HealCommand;
import me.dmk.core.command.implementation.admin.InvseeCommand;
import me.dmk.core.command.implementation.admin.KickCommand;
import me.dmk.core.command.implementation.admin.SetSpawnCommand;
import me.dmk.core.command.implementation.admin.SpeedCommand;
import me.dmk.core.command.implementation.admin.TeleportCommand;
import me.dmk.core.command.implementation.admin.VanishCommand;
import me.dmk.core.command.implementation.admin.punishment.BanCommand;
import me.dmk.core.command.implementation.admin.punishment.MuteCommand;
import me.dmk.core.command.implementation.admin.punishment.TempBanCommand;
import me.dmk.core.command.implementation.admin.punishment.TempMuteCommand;
import me.dmk.core.command.implementation.admin.punishment.UnBanCommand;
import me.dmk.core.command.implementation.admin.punishment.UnMuteCommand;
import me.dmk.core.command.implementation.guild.GuildCreateCommand;
import me.dmk.core.command.implementation.guild.GuildCreateRankCommand;
import me.dmk.core.command.implementation.guild.GuildDeleteCommand;
import me.dmk.core.command.implementation.guild.GuildDepositCommand;
import me.dmk.core.command.implementation.guild.GuildExtendCommand;
import me.dmk.core.command.implementation.guild.GuildForceDeleteCommand;
import me.dmk.core.command.implementation.guild.GuildInviteCommand;
import me.dmk.core.command.implementation.guild.GuildJoinCommand;
import me.dmk.core.command.implementation.guild.GuildKickCommand;
import me.dmk.core.command.implementation.guild.GuildLeaveCommand;
import me.dmk.core.command.implementation.guild.GuildPanelCommand;
import me.dmk.core.command.implementation.guild.alliance.GuildAllianceCommand;
import me.dmk.core.command.implementation.player.FriendCommand;
import me.dmk.core.command.implementation.player.GroupsCommand;
import me.dmk.core.command.implementation.player.IgnoreCommand;
import me.dmk.core.command.implementation.player.IncognitoCommand;
import me.dmk.core.command.implementation.player.KitCommand;
import me.dmk.core.command.implementation.player.MessageCommand;
import me.dmk.core.command.implementation.player.PingCommand;
import me.dmk.core.command.implementation.player.ProfileCommand;
import me.dmk.core.command.implementation.player.ReplyCommand;
import me.dmk.core.command.implementation.player.ResetStatisticsCommand;
import me.dmk.core.command.implementation.player.SidebarCommand;
import me.dmk.core.command.implementation.player.SpawnCommand;
import me.dmk.core.command.implementation.player.TopsCommand;
import me.dmk.core.command.implementation.player.WeatherCommand;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.database.MongoClientService;
import me.dmk.core.database.data.MongoDataService;
import me.dmk.core.database.data.adapter.JsonDateAdapter;
import me.dmk.core.database.data.serializer.GsonSerializer;
import me.dmk.core.database.data.serializer.GsonSerializerImpl;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.guild.member.GuildMember;
import me.dmk.core.guild.task.GuildExpirationTimeTask;
import me.dmk.core.guild.task.GuildSaveTask;
import me.dmk.core.kit.KitMap;
import me.dmk.core.listener.EntityDamageByEntityListener;
import me.dmk.core.listener.EntityResurrectListener;
import me.dmk.core.listener.SignChangeListener;
import me.dmk.core.listener.luckperms.LuckPermsListener;
import me.dmk.core.listener.motd.MotdPacketListener;
import me.dmk.core.listener.player.AsyncPlayerChatListener;
import me.dmk.core.listener.player.PlayerCommandPreprocessListener;
import me.dmk.core.listener.player.PlayerDeathListener;
import me.dmk.core.listener.player.PlayerInteractListener;
import me.dmk.core.listener.player.PlayerItemConsumeListener;
import me.dmk.core.listener.player.PlayerLevelChangeListener;
import me.dmk.core.listener.player.PlayerMoveListener;
import me.dmk.core.listener.player.PrivateMessageListener;
import me.dmk.core.listener.player.connection.PlayerJoinListener;
import me.dmk.core.listener.player.connection.PlayerLoginListener;
import me.dmk.core.listener.player.connection.PlayerQuitListener;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.murder.MurderCache;
import me.dmk.core.nametag.map.NameTagMap;
import me.dmk.core.nametag.task.NameTagUpdateTask;
import me.dmk.core.nametag.updater.NametagUpdater;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.settings.board.BoardTask;
import me.dmk.core.profile.settings.incognito.IncognitoController;
import me.dmk.core.profile.task.ProfileRefreshTask;
import me.dmk.core.profile.task.SaveProfileTask;
import me.dmk.core.task.executor.TaskExecutor;
import me.dmk.core.task.executor.TaskExecutorImpl;
import me.dmk.core.teleport.TeleportMap;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.skinsrestorer.api.SkinsRestorerAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * Created by DMK on 28.12.2022
 */

@Getter
public class CorePlugin extends JavaPlugin {

    @Getter
    private static CorePlugin corePlugin;

    private PluginConfiguration pluginConfiguration;

    private LuckPerms luckPerms;
    private MiniMessage miniMessage;
    private BukkitAudiences bukkitAudiences;
    private SkinsRestorerAPI skinsRestorerAPI;

    private MongoClientService mongoClientService;
    private MongoDataService mongoDataService;

    private LuckPermsController luckPermsController;
    private NotificationController notificationController;
    private ProfileController profileController;
    private GuildController guildController;
    private IncognitoController incognitoController;

    private GlobalChatCache globalChatCache;
    private MurderCache murderCache;
    private ChatWaiterCache chatWaiterCache;

    private KitMap kitMap;
    private TeleportMap teleportMap;
    private NameTagMap nametagMap;

    private NametagUpdater nametagUpdater;

    private LiteCommands<CommandSender> liteCommands;

    private TaskExecutor taskExecutor;

    @Override
    public void onEnable() {
        corePlugin = this;
        long start = System.currentTimeMillis();

        /* Configuration */
        this.pluginConfiguration = ConfigManager.create(PluginConfiguration.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            it.withBindFile(new File(this.getDataFolder(), "configuration.yml"));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });

        /* Libraries */
        RegisteredServiceProvider<LuckPerms> luckPermsProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (luckPermsProvider == null) {
            this.getLogger().severe("LuckPerms not found! Plugin will be disabled.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.luckPerms = luckPermsProvider.getProvider();
        this.miniMessage = MiniMessage.miniMessage();
        this.bukkitAudiences = BukkitAudiences.create(this);
        this.skinsRestorerAPI = SkinsRestorerAPI.getApi();

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        /* Gson serializer */
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new JsonDateAdapter())
                .setLongSerializationPolicy(LongSerializationPolicy.STRING)
                .serializeNulls()
                .create();

        GsonSerializer gsonSerializer = new GsonSerializerImpl(gson);

        /* Services */
        this.mongoClientService = new MongoClientService(this, this.pluginConfiguration.getDatabaseConfiguration());
        this.mongoClientService.connect();

        this.mongoDataService = new MongoDataService(this.getLogger(), gsonSerializer, this.mongoClientService);

        /* Controllers */
        this.luckPermsController = new LuckPermsController(luckPerms);
        this.notificationController = new NotificationController(this.bukkitAudiences, this.miniMessage);
        this.profileController = new ProfileController(this.mongoDataService);
        this.guildController = new GuildController(this.mongoDataService);
        this.incognitoController = new IncognitoController(this.mongoDataService, this.skinsRestorerAPI);

        /* Cache */
        this.globalChatCache = new GlobalChatCache();
        this.murderCache = new MurderCache();
        this.chatWaiterCache = new ChatWaiterCache();

        Bukkit.getScheduler().runTask(this, this::addOnlinePlayersToCache);

        /* Maps */
        this.kitMap = new KitMap(this.pluginConfiguration.getKitConfiguration());
        this.kitMap.loadKitsFromConfiguration();

        this.teleportMap = new TeleportMap();
        this.nametagMap = new NameTagMap();

        /* Updaters */
        this.nametagUpdater = new NametagUpdater(this.luckPermsController, this.profileController, this.nametagMap);

        /* Tasks */
        this.taskExecutor = new TaskExecutorImpl();

        this.taskExecutor.runTimerAsync(new BoardTask(this.profileController), 5L, TimeUnit.SECONDS);
        this.taskExecutor.runTimerAsync(new ProfileRefreshTask(this.pluginConfiguration, this.miniMessage, this.notificationController, this.profileController, this.taskExecutor), 1L, TimeUnit.SECONDS);
        this.taskExecutor.runTimerAsync(new SaveProfileTask(this.profileController), 20L, TimeUnit.MINUTES);
        this.taskExecutor.runTimerAsync(new GuildExpirationTimeTask(this.mongoDataService, this.notificationController, this.guildController), 1L, TimeUnit.MINUTES);
        this.taskExecutor.runTimerAsync(new GuildSaveTask(this.guildController), 15L, TimeUnit.MINUTES);
        this.taskExecutor.runTimerAsync(new NameTagUpdateTask(this.nametagUpdater), 5L, TimeUnit.SECONDS);

        /* Commands */
        this.liteCommands = this.registerLiteCommands();

        /* Listeners */
        Stream.of(
                new PlayerJoinListener(this.pluginConfiguration, this.notificationController, this.profileController, this.guildController, this.kitMap, this.nametagMap),
                new PlayerLoginListener(this.profileController),
                new PlayerQuitListener(this.profileController, this.nametagMap, this.taskExecutor),

                new AsyncPlayerChatListener(this.miniMessage, this.luckPermsController, this.notificationController, this.profileController, this.guildController, this.globalChatCache, this.chatWaiterCache),
                new EntityDamageByEntityListener(this.pluginConfiguration, this.notificationController, this.profileController, this.teleportMap),
                new EntityResurrectListener(this.notificationController, this.profileController),
                new PlayerCommandPreprocessListener(this.pluginConfiguration, this.notificationController, this.profileController),
                new PlayerDeathListener(this.notificationController, this.profileController, this.murderCache, this.kitMap),
                new PlayerInteractListener(this.profileController),
                new PlayerItemConsumeListener(this.profileController),
                new PlayerLevelChangeListener(this.notificationController, this.profileController),
                new PlayerMoveListener(this.teleportMap),
                new PrivateMessageListener(this.notificationController),
                new SignChangeListener(this.miniMessage)
        ).forEach(listener -> Bukkit.getServer().getPluginManager().registerEvents(listener, this));

        new MotdPacketListener(this, this.pluginConfiguration.getMotdConfiguration(), protocolManager, this.miniMessage);
        new LuckPermsListener(this.notificationController, this.taskExecutor, this.luckPerms);

        this.getLogger().info("Loaded plugin in " + (System.currentTimeMillis() - start) + " ms.");
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);

        Bukkit.getOnlinePlayers().forEach(player ->
                this.profileController.get(player.getUniqueId()).ifPresent(profile -> {
                    this.profileController.save(profile);
                    profile.getGuild().ifPresent(guildController::save);
                })
        );

        this.mongoClientService.close();
        this.bukkitAudiences.close();
        this.taskExecutor.shutdownNow();
        this.liteCommands.getPlatform().unregisterAll();

        this.getLogger().info("Goodbye!");
    }

    private LiteCommands<CommandSender> registerLiteCommands() {
        return LiteBukkitAdventurePlatformFactory.builder(this.getServer(), this.getName(), true, this.bukkitAudiences, true)
                .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>("&cNie możesz użyć tej komendy."))

                .invalidUsageHandler(new InvalidUsageHandler(this.notificationController))
                .permissionHandler(new MissingPermissionHandler(this.notificationController))

                .argument(GuildMember.class, new GuildMemberArgument(this.profileController, this.miniMessage))
                .argument(Guild.class, new GuildArgument(this.guildController, this.miniMessage))

                .argument(NotificationType.class, new NotificationTypeArgument(this.miniMessage))

                .argument(GameMode.class, new GameModeArgument(this.miniMessage))
                .argument(Instant.class, new InstantArgument(this.miniMessage))
                .argument(Integer.class, new IntegerArgument(this.miniMessage))
                .argumentMultilevel(Location.class, new LocationArgument(this.miniMessage))
                .argument(Player.class, new PlayerArgument(this.miniMessage))

                .argument(Profile.class, new ProfileArgument(this.profileController, this.miniMessage))

                .contextualBind(Profile.class, new ProfileContextual(this.profileController))
                .contextualBind(Guild.class, new GuildContextual(this.profileController, this.miniMessage))

                .commandInstance(
                        new BanCommand(this.notificationController, this.profileController),
                        new BroadCastCommand(this.miniMessage, this.notificationController),
                        new ChatCommand(this.notificationController, this.globalChatCache),
                        new ClearCommand(this.notificationController),
                        new FlyCommand(this.notificationController),
                        new GameModeCommand(this.notificationController),
                        new GodModeCommand(this.notificationController, this.profileController),
                        new HealCommand(this.notificationController),
                        new InvseeCommand(),
                        new KickCommand(this.notificationController),
                        new MuteCommand(this.notificationController, this.profileController),
                        new SpeedCommand(this.notificationController),
                        new SetSpawnCommand(this.notificationController),
                        new TeleportCommand(this.notificationController),
                        new TempBanCommand(this.notificationController, this.profileController),
                        new TempMuteCommand(this.notificationController, this.profileController),
                        new UnBanCommand(this.notificationController, this.profileController),
                        new UnMuteCommand(this.notificationController, this.profileController),
                        new VanishCommand(this.notificationController, this.profileController),

                        new GuildAllianceCommand(this.notificationController, this.guildController),

                        new GuildCreateCommand(this.pluginConfiguration, this.notificationController, this.guildController),
                        new GuildCreateRankCommand(this.notificationController, this.guildController),
                        new GuildDeleteCommand(this.notificationController, this.guildController, this.taskExecutor),
                        new GuildDepositCommand(this.notificationController, this.profileController,this.guildController),
                        new GuildExtendCommand(this.pluginConfiguration, this.notificationController, this.guildController, this.taskExecutor),
                        new GuildForceDeleteCommand(this.notificationController, this.guildController, this.taskExecutor),
                        new GuildPanelCommand(),
                        new GuildInviteCommand(this.notificationController),
                        new GuildJoinCommand(this.notificationController, this.profileController,this.guildController),
                        new GuildKickCommand(this.notificationController, this.profileController, this.guildController),
                        new GuildLeaveCommand(this.notificationController, this.profileController, this.guildController),

                        new FriendCommand(this.notificationController),
                        new GroupsCommand(this.luckPermsController, this.notificationController),
                        new IgnoreCommand(this.notificationController, this.profileController),
                        new IncognitoCommand(this.notificationController, this.profileController, this.incognitoController),
                        new KitCommand(this.notificationController, this.profileController, this.kitMap),
                        new MessageCommand(this.notificationController, this.profileController),
                        new PingCommand( this.notificationController),
                        new ProfileCommand(),
                        new ReplyCommand(this.notificationController, this.profileController),
                        new ResetStatisticsCommand(this.pluginConfiguration,  this.notificationController,  this.profileController, this.taskExecutor),
                        new SidebarCommand(this.notificationController),
                        new SpawnCommand(this.notificationController, this.teleportMap),
                        new TopsCommand(),
                        new WeatherCommand()
                )
                .register();
    }

    private void addOnlinePlayersToCache() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.profileController.findByUUIDOrElseCreate(player.getUniqueId(), player.getName())
                    .getGuild().ifPresent(guildController::add);
        }
    }
}

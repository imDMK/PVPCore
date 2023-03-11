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
import me.dmk.core.command.argument.player.*;
import me.dmk.core.command.argument.profile.ProfileArgument;
import me.dmk.core.command.contextual.GuildContextual;
import me.dmk.core.command.contextual.ProfileContextual;
import me.dmk.core.command.handler.InvalidUsageHandler;
import me.dmk.core.command.handler.MissingPermissionHandler;
import me.dmk.core.command.implementation.admin.*;
import me.dmk.core.command.implementation.admin.punishment.*;
import me.dmk.core.command.implementation.guild.*;
import me.dmk.core.command.implementation.guild.alliance.GuildAllianceCommand;
import me.dmk.core.command.implementation.player.*;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.database.MongoClientService;
import me.dmk.core.database.data.MongoDataService;
import me.dmk.core.database.data.adapter.JsonDateAdapter;
import me.dmk.core.database.data.serializer.GsonSerializer;
import me.dmk.core.database.data.serializer.GsonSerializerImpl;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.guild.controller.GuildController;
import me.dmk.core.guild.member.GuildMember;
import me.dmk.core.guild.task.GuildExpirationTimeTask;
import me.dmk.core.kit.KitMap;
import me.dmk.core.listener.*;
import me.dmk.core.listener.connection.PlayerJoinListener;
import me.dmk.core.listener.connection.PlayerLoginListener;
import me.dmk.core.listener.connection.PlayerQuitListener;
import me.dmk.core.listener.luckperms.LuckPermsListener;
import me.dmk.core.listener.motd.MotdPacketListener;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.murder.MurderCache;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.settings.board.BoardTask;
import me.dmk.core.profile.settings.incognito.IncognitoController;
import me.dmk.core.profile.task.ProfileTask;
import me.dmk.core.profile.task.SaveProfileTask;
import me.dmk.core.task.executor.TaskExecutor;
import me.dmk.core.task.executor.TaskExecutorImpl;
import me.dmk.core.teleport.TeleportMap;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;
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
import java.util.Collection;
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

    private ProfileCache profileCache;
    private GuildCache guildCache;
    private GlobalChatCache globalChatCache;
    private MurderCache murderCache;
    private ChatWaiterCache chatWaiterCache;

    private KitMap kitMap;
    private TeleportMap teleportMap;

    private TaskExecutor taskExecutor;

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        corePlugin = this;
        long start = System.currentTimeMillis();

        this.pluginConfiguration = ConfigManager.create(PluginConfiguration.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            it.withBindFile(new File(this.getDataFolder(), "configuration.yml"));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });

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
        this.profileCache = new ProfileCache(this.profileController);
        this.guildCache = new GuildCache(this.guildController);
        this.globalChatCache = new GlobalChatCache();
        this.murderCache = new MurderCache();
        this.chatWaiterCache = new ChatWaiterCache();

        /* Maps */
        this.kitMap = new KitMap(this.pluginConfiguration.getKitConfiguration());
        this.kitMap.loadKitsFromConfiguration();

        this.teleportMap = new TeleportMap();

        /* Tasks */
        this.taskExecutor = new TaskExecutorImpl();

        this.taskExecutor.runTimerAsync(new BoardTask(this.profileCache), 5L, TimeUnit.SECONDS);
        this.taskExecutor.runTimerAsync(new SaveProfileTask(this.profileController, this.profileCache), 20L, TimeUnit.MINUTES);
        this.taskExecutor.runTimerAsync(new ProfileTask(this.pluginConfiguration, this.miniMessage, this.notificationController, this.profileCache, this.getTaskExecutor()), 1L, TimeUnit.SECONDS);
        this.taskExecutor.runTimerAsync(new GuildExpirationTimeTask(this.mongoDataService, this.notificationController, this.guildController, this.guildCache), 1L, TimeUnit.MINUTES);

        /* Commands */
        this.liteCommands = this.registerLiteCommands();

        /* Listeners */
        Stream.of(
                new PlayerJoinListener(this.pluginConfiguration, this.notificationController, this.profileCache, this.guildCache, this.kitMap),
                new PlayerLoginListener(this.profileCache),
                new PlayerQuitListener(this.profileController, this.profileCache, this.taskExecutor),

                new AsyncPlayerChatListener(this.miniMessage, this.luckPermsController, this.notificationController, this.profileCache, this.guildCache, this.globalChatCache, this.chatWaiterCache),
                new EntityDamageByEntityListener(this.pluginConfiguration, this.notificationController, this.profileCache, this.teleportMap),
                new EntityResurrectListener(this.notificationController, this.profileCache),
                new PlayerCommandPreprocessListener(this.pluginConfiguration, this.notificationController, this.profileCache),
                new PlayerDeathListener(this.notificationController, this.profileCache, this.murderCache, this.kitMap),
                new PlayerInteractListener(this.profileCache),
                new PlayerItemConsumeListener(this.profileCache),
                new PlayerLevelChangeListener(this.notificationController, this.profileCache),
                new PlayerMoveListener(this.teleportMap),
                new PrivateMessageListener(this.notificationController),
                new SignChangeListener(this.miniMessage)
        ).forEach(listener -> Bukkit.getServer().getPluginManager().registerEvents(listener, this));

        new MotdPacketListener(this, this.pluginConfiguration.getMotdConfiguration(), protocolManager, this.miniMessage);
        LuckPermsListener luckPermsListener = new LuckPermsListener(this.notificationController, this.taskExecutor);

        EventBus eventBus = luckPerms.getEventBus();
        eventBus.subscribe(this, NodeAddEvent.class, luckPermsListener::onNodeAdd);
        eventBus.subscribe(this, NodeRemoveEvent.class, luckPermsListener::onNodeRemove);

        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        if (!onlinePlayers.isEmpty()) {
            for (Player player : onlinePlayers) {
                Profile profile = this.profileController.findByUUIDOrCreate(player.getUniqueId(), player.getName());

                this.profileCache.add(profile);
                profile.getGuild().ifPresent(guildCache::add);
            }

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chat clear");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "broadcast SUBTITLE <green>Serwer został przeładowany.");
        }

        this.getLogger().info("Loaded plugin in " + (System.currentTimeMillis() - start) + " ms.");
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            this.profileCache.get(player.getUniqueId()).ifPresent(profileController::save);
        }

        this.guildCache.getGuilds().forEach(guildController::save);

        this.mongoClientService.close();
        this.taskExecutor.shutdownNow();
        this.liteCommands.getPlatform().unregisterAll();

        this.getLogger().info("Goodbye!");
    }

    private LiteCommands<CommandSender> registerLiteCommands() {
        return LiteBukkitAdventurePlatformFactory.builder(this.getServer(), this.getName(), true, this.bukkitAudiences, true)
                .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>("&cNie możesz użyć tej komendy."))

                .invalidUsageHandler(new InvalidUsageHandler(this.notificationController))
                .permissionHandler(new MissingPermissionHandler(this.notificationController))

                .argument(GuildMember.class, new GuildMemberArgument(this.profileCache, this.miniMessage))
                .argument(Guild.class, new GuildArgument(this.guildCache, this.miniMessage))

                .argument(NotificationType.class, new NotificationTypeArgument(this.miniMessage))

                .argument(GameMode.class, new GameModeArgument(this.miniMessage))
                .argument(Instant.class, new InstantArgument(this.miniMessage))
                .argument(Integer.class, new IntegerArgument(this.miniMessage))
                .argumentMultilevel(Location.class, new LocationArgument(this.miniMessage))
                .argument(Player.class, new PlayerArgument(this.miniMessage))

                .argument(Profile.class, new ProfileArgument(this.profileCache, this.miniMessage))

                .contextualBind(Profile.class, new ProfileContextual(this.profileCache))
                .contextualBind(Guild.class, new GuildContextual(this.profileCache, this.miniMessage))

                .commandInstance(
                        new BanCommand(this.notificationController, this.profileController),
                        new BroadCastCommand(this.miniMessage, this.notificationController),
                        new ChatCommand(this.notificationController, this.globalChatCache),
                        new ClearCommand(this.notificationController),
                        new FlyCommand(this.notificationController),
                        new GameModeCommand(this.notificationController),
                        new GodModeCommand(this.notificationController, this.profileCache),
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
                        new VanishCommand(this.notificationController, this.profileCache),

                        new GuildAllianceCommand(this.notificationController, this.guildController),

                        new GuildCreateCommand(this.pluginConfiguration, this.notificationController, this.guildController, this.guildCache),
                        new GuildDeleteCommand(this.notificationController, this.guildController, this.guildCache, this.taskExecutor),
                        new GuildDepositCommand(this.notificationController, this.profileController,this.guildController),
                        new GuildExtendCommand(this.pluginConfiguration, this.notificationController, this.guildController, this.taskExecutor),
                        new GuildForceDeleteCommand(this.notificationController, this.guildController, this.guildCache, this.taskExecutor),
                        new GuildInformationCommand(),
                        new GuildInviteCommand(this.notificationController),
                        new GuildJoinCommand(this.notificationController, this.profileController,this.guildController),
                        new GuildKickCommand(this.notificationController, this.profileController, this.guildController, this.profileCache),
                        new GuildLeaveCommand(this.notificationController, this.profileController, this.guildController),

                        new GroupsCommand(this.luckPermsController, this.notificationController),
                        new IgnoreCommand(this.notificationController, this.profileCache),
                        new IncognitoCommand(this.notificationController, this.profileController, this.incognitoController),
                        new KitCommand(this.notificationController, this.profileController, this.kitMap),
                        new MessageCommand(this.notificationController, this.profileCache),
                        new PingCommand( this.notificationController),
                        new ProfileCommand(),
                        new ReplyCommand(this.notificationController, this.profileCache),
                        new ResetStatisticsCommand(this.pluginConfiguration,  this.notificationController,  this.profileController, this.taskExecutor),
                        new SidebarCommand(this.notificationController),
                        new SpawnCommand(this.notificationController, this.teleportMap),
                        new TopsCommand()
                )
                .register();
    }
}

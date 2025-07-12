package net.uebliche.server;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.instance.AddEntityToInstanceEvent;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.network.packet.client.play.ClientChangeGameModePacket;
import net.uebliche.mode.ModeSettings;
import net.uebliche.mode.lobby.Lobby;
import net.uebliche.server.abyss.Abyss;
import net.uebliche.server.commands.GameModeCommand;
import net.uebliche.server.commands.LeaveCommand;
import net.uebliche.server.commands.StopCommand;
import net.uebliche.server.commands.SurvivalCommand;
import net.uebliche.server.mongodb.codec.InstantCodec;
import net.uebliche.server.mongodb.codec.LocaleCodec;
import net.uebliche.server.mongodb.objects.ban.Ban;
import net.uebliche.server.mongodb.objects.ban.BanRepository;
import net.uebliche.server.mongodb.objects.settings.SettingsRepository;
import net.uebliche.server.mongodb.objects.user.User;
import net.uebliche.server.mongodb.objects.user.UserRepository;
import net.uebliche.server.mongodb.objects.world.World;
import net.uebliche.server.mongodb.objects.world.WorldRepository;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GameServer {

    private static final Logger log = LoggerFactory.getLogger(GameServer.class);
    private static GameServer instance;

    private UserRepository userRepository;
    private SettingsRepository settingsRepository;
    private WorldRepository worldRepository;
    private BanRepository banRepository;

    private static final String HOST = System.getenv().getOrDefault("HOST", "0.0.0.0");
    private static final Integer PORT = Integer.parseInt(System.getenv().getOrDefault("PORT", "25565"));
    // Visit https://cloud.mongodb.com to get your own DB for free online at MongoDB
    private static final String MONGODB_URI = System.getenv().getOrDefault("MONGODB_URI", "mongodb://localhost:27017");
    private static final String MONGODB_DATABASE = System.getenv().getOrDefault("MONGODB_DATABASE", "minestom");

    private final Abyss abyss;
    private final Lobby lobby;


    private GameServer() {
        instance = this;
        MinecraftServer.setCompressionThreshold(0);
        MinecraftServer minecraftServer = MinecraftServer.init();
        MojangAuth.init();

        setupDatabaseConnection();

        abyss = new Abyss(this);
        lobby = new Lobby(this);

        //GameRegistry.registerGame(Survival.class);

        registerGlobalEventListeners();
        registerCommands();
        registerPlayerProvider();

        minecraftServer.start(HOST, PORT);
    }

    private void registerGlobalEventListeners() {
        MinecraftServer.getGlobalEventHandler()
                .addListener(AsyncPlayerConfigurationEvent.class, event -> {
                    MinecraftServer.getInstanceManager().getInstances().stream().findFirst().ifPresentOrElse(instance -> {
                        event.setSpawningInstance(abyss);
                        event.getPlayer().setPermissionLevel(3);
                    }, () -> {
                        log.warn("Failed to find instance to spawn player");
                    });
                })
                .addListener(PlayerMoveEvent.class, event -> {
                    var player = event.getPlayer();
                    if (event.getNewPosition().y() < -130) {
                        player.damage(DamageType.OUT_OF_WORLD, 3);
                    }
                })
                .addListener(AddEntityToInstanceEvent.class, event -> {
                    if (event.getEntity() instanceof Player player)
                        player.refreshCommands();
                })
        ;
        MinecraftServer.getPacketListenerManager().setPlayListener(ClientChangeGameModePacket.class,
                (packet, player) -> {
                    player.setGameMode(packet.gameMode());

                });
    }

    public Lobby getLobby() {
        return lobby;
    }

    private void setupDatabaseConnection() {
        CodecRegistry customRegistry = CodecRegistries.fromCodecs(new InstantCodec(), new LocaleCodec());
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
        var combinedCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                customRegistry,
                pojoCodecRegistry
        );
        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(combinedCodecRegistry)
                .applyConnectionString(new ConnectionString(MONGODB_URI))
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(MONGODB_DATABASE);

        userRepository = new UserRepository(mongoDatabase.getCollection("user", User.class));
        settingsRepository = new SettingsRepository(mongoDatabase.getCollection("settings", ModeSettings.class));
        worldRepository = new WorldRepository(mongoDatabase.getCollection("world", World.class));
        banRepository = new BanRepository(mongoDatabase.getCollection("ban", Ban.class));

    }


    private void registerPlayerProvider() {
        MinecraftServer.getConnectionManager()
                .setPlayerProvider(
                        (playerConnection, gameProfile) -> {
                            var player = new GamePlayer(
                                    playerConnection,
                                    gameProfile,
                                    userRepository
                                            .findOrCreate(gameProfile)
                                            .join()
                            );
                            userRepository.incrementLoginCount(gameProfile.uuid()).thenAccept(user -> {
                                log.debug("Login Count for {} +1", player.getUsername());
                            });
                            return player;
                        }
                );
    }

    private void registerCommands() {
        var commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new GameModeCommand());
        commandManager.register(new StopCommand());
        commandManager.register(new LeaveCommand());
        commandManager.register(new SurvivalCommand());
    }

    public static GameServer init() {
        if (instance != null) {
            throw new IllegalStateException("GameServer already initialized");
        }
        return new GameServer();
    }

    public static GameServer getInstance() {
        return instance;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public BanRepository getBanRepository() {
        return banRepository;
    }

    public WorldRepository getWorldRepository() {
        return worldRepository;
    }

    public SettingsRepository getSettingsRepository() {
        return settingsRepository;
    }
}

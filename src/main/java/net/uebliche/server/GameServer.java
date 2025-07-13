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
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.uebliche.mode.ModeSettings;
import net.uebliche.server.abyss.Abyss;
import net.uebliche.server.commands.GameModeCommand;
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
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GameServer<P extends GamePlayer> {

    private final Logger log = LoggerFactory.getLogger(GameServer.class);

    protected UserRepository userRepository;
    protected SettingsRepository settingsRepository;
    protected WorldRepository worldRepository;
    protected BanRepository banRepository;

    private static final String HOST = System.getenv().getOrDefault("HOST", "0.0.0.0");
    private static final Integer PORT = Integer.parseInt(System.getenv().getOrDefault("PORT", "25565"));
    // Visit https://cloud.mongodb.com to get your own DB for free online at MongoDB
    private static final String MONGODB_URI = System.getenv().getOrDefault("MONGODB_URI", "mongodb://localhost:27017");
    private static final String MONGODB_DATABASE = System.getenv().getOrDefault("MONGODB_DATABASE", "minestom");

    private final Abyss<P> abyss;

    private static GameServer instance;

    public GameServer() {
        if (instance != null)
            throw new IllegalStateException("Only one instance of GameServer is allowed");
        instance = this;
        MinecraftServer.setCompressionThreshold(0);
        MinecraftServer minecraftServer = MinecraftServer.init();
        MojangAuth.init();

        setupDatabaseConnection();

        abyss = new Abyss<P>(this, this::onPlayerLoaded);

        registerGlobalEventListeners();
        registerCommands();
        MinecraftServer.getConnectionManager()
                .setPlayerProvider(this::getPlayerProvider);

        minecraftServer.start(HOST, PORT);
    }

    public static GameServer getInstance() {
        return instance;
    }

    protected abstract @NotNull P getPlayerProvider(@NotNull PlayerConnection playerConnection,
                                                    @NotNull GameProfile gameProfile);

    protected abstract void onPlayerLoaded(P player, Boolean success);

    private void registerGlobalEventListeners() {
        MinecraftServer.getGlobalEventHandler()
                .addListener(AsyncPlayerConfigurationEvent.class, event -> {
                    event.setSpawningInstance(abyss);
                    event.getPlayer().setPermissionLevel(3);
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

    private void registerCommands() {
        var commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new GameModeCommand());
        commandManager.register(new StopCommand());
        commandManager.register(new SurvivalCommand());
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

package net.uebliche.demo;

import dev.lu15.voicechat.VoiceChat;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.uebliche.demo.commands.LeaveCommand;
import net.uebliche.demo.game.ffa.ClassicFFA;
import net.uebliche.demo.game.ffa.ClassicFFASettings;
import net.uebliche.demo.game.survival.Survival;
import net.uebliche.demo.lobby.Lobby;
import net.uebliche.game.GameRegistry;
import net.uebliche.server.GameServer;
import net.uebliche.demo.commands.SurvivalCommand;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DemoServer serves as a concrete implementation of the abstract {@code GameServer} class,
 * specifically using {@code DemoPlayer} as the player provider. It initializes the
 * necessary components for managing game modes, commands, and player settings.
 */
public class DemoServer extends GameServer<DemoPlayer> {

    private static final Logger log = LoggerFactory.getLogger(DemoServer.class);

    public static void main(String[] args) {
        new DemoServer();
    }

    private final Lobby lobby;

    public DemoServer() {
        lobby = new Lobby(this);
        VoiceChat.builder(HOST, PORT).enable();

        registerGames();
        testSettings();
    }

    private void registerGames() {
        GameRegistry.registerGame(Survival.class);
        GameRegistry.registerGame(ClassicFFA.class);
    }

    @Override
    protected void registerCommands() {
        super.registerCommands();
        commandManager.register(new LeaveCommand(() -> lobby));
        commandManager.register(new SurvivalCommand(() -> GameRegistry.findGame(Survival.class)));
    }

    @Override
    protected @NotNull DemoPlayer getPlayerProvider(@NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile) {
        var player = new DemoPlayer(playerConnection, gameProfile, userRepository.findOrCreate(gameProfile).join());
        userRepository.incrementLoginCount(gameProfile.uuid()).thenAccept(success -> {
            log.debug("Login Count for {} +1", player.getUsername());
        });
        return player;
    }

    @Override
    protected void onPlayerLoaded(DemoPlayer player, Boolean success) {
        if (success) lobby.enter(player);
        else player.kick("Failed to load");
    }

    private void testSettings() {
//        var classicFFA = new ClassicFFASettings();
//        classicFFA.testClassicString = "ja";
//        settingsRepository.insert(classicFFA);

        var id = new ObjectId("68710dc35ebf0d9ddaaec68b");
        var found = settingsRepository.find(id, ClassicFFASettings.class);
        log.info("Found settings: {}", found);

    }

}

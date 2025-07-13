package net.uebliche.demo;

import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.uebliche.demo.game.ffa.ClassicFFA;
import net.uebliche.demo.game.ffa.ClassicFFASettings;
import net.uebliche.demo.game.survival.Survival;
import net.uebliche.demo.lobby.Lobby;
import net.uebliche.game.GameRegistry;
import net.uebliche.server.GamePlayer;
import net.uebliche.server.GameServer;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoServer extends GameServer<DemoPlayer> {

    private static final Logger log = LoggerFactory.getLogger(DemoServer.class);

    public static void main(String[] args) {
        new DemoServer();
    }

    private Lobby lobby;

    public DemoServer() {
        lobby = new Lobby(this);
        testSettings();
        GameRegistry.registerGame(Survival.class);
        GameRegistry.registerGame(ClassicFFA.class);
    }

    @Override
    protected @NotNull DemoPlayer getPlayerProvider(@NotNull PlayerConnection playerConnection,
                                                    @NotNull GameProfile gameProfile) {
        var player = new DemoPlayer(
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

    @Override
    protected void onPlayerLoaded(DemoPlayer player, Boolean success) {
        if (success)
            lobby.enter(player);
        else player.kick("Failed to load");
    }

    private void testSettings() {
//        var classicFFA = new ClassicFFASettings();
//        classicFFA.testClassicString = "ja";
//        settingsRepository.insert(classicFFA);

//        var id = new ObjectId("68710dc35ebf0d9ddaaec68b");
//        var found = settingsRepository.find(id, ClassicFFASettings.class);
//        log.info("Found settings: {}", found);

    }

}

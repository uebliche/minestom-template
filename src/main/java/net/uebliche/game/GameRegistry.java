package net.uebliche.game;

import net.minestom.server.instance.InstanceContainer;
import net.uebliche.mode.Mode;
import net.uebliche.mode.ModeSettings;
import net.uebliche.server.GameServer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class GameRegistry {

    private static final List<Class<? extends Game<GameSettings>>> gameClazzs = new ArrayList<>();
    private static final HashMap<Class<? extends Game<GameSettings>>, HashMap<UUID, ? extends Game<GameSettings>>> gameInstances = new HashMap<>();

    private static final HashMap<UUID, Game<GameSettings>> gamesById = new HashMap<>();


    public static void registerGame(Class<? extends Game<GameSettings>> gameClazz) {
        gameClazzs.add(gameClazz);
    }

    public static void register(Game<GameSettings> game) {
        gamesById.put(game.getId(), game);
    }

    public static Game<GameSettings> get(UUID uuid){
        return (Game<GameSettings>) gamesById.get(uuid);
    }

    public static void unregister(UUID uuid) {
        gamesById.remove(uuid);
    }

    public static <G extends Game<GameSettings>> G startGame(Class<G> gameClazz) throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        if (!gameClazzs.contains(gameClazz)) {
            throw new IllegalArgumentException("Game class not registered");
        }
        var game = gameClazz.getConstructor(GameServer.class).newInstance(GameServer.getInstance());
        gamesById.put(game.getId(), game);
        return game;
    }

}

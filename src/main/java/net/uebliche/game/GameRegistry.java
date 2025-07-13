package net.uebliche.game;

import net.uebliche.server.GameServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class GameRegistry {

    private static final List<Class<? extends Game<?>>> gameClazzs = new ArrayList<>();
    private static final HashMap<Class<? extends Game<?>>, HashMap<UUID, ? extends Game<?>>> gameInstances = new HashMap<>();

    private static final HashMap<UUID, Game<?>> gamesById = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(GameRegistry.class);


    public static void registerGame(Class<? extends Game<?>> gameClazz) {
        gameClazzs.add(gameClazz);
    }

    public static void register(Game<GameSettings> game) {
        gamesById.put(game.getId(), game);
    }

    public static Game<GameSettings> get(UUID uuid) {
        return (Game<GameSettings>) gamesById.get(uuid);
    }

    public static void unregister(UUID uuid) {
        gamesById.remove(uuid);
    }

    public static <G extends Game<?>> G startGame(Class<G> gameClazz) throws NoSuchMethodException,
            InvocationTargetException,
            InstantiationException, IllegalAccessException {
        if (!gameClazzs.contains(gameClazz)) {
            log.error("Game class {} not registered", gameClazz);
            throw new IllegalArgumentException("Game class not registered");
        }
        var game = gameClazz.getConstructor(GameServer.class).newInstance(GameServer.getInstance());
        gamesById.put(game.getId(), game);
        return game;
    }

    public static <G extends Game<?>> G findGame(Class<G> gameClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (!gameClazzs.contains(gameClass)) {
            log.error("Game class {} not registered", gameClass);
            throw new IllegalArgumentException("Game class not registered");
        }
        var game = gameInstances.get(gameClass);
        if (game == null) {
            game = new HashMap<>();
            gameInstances.put(gameClass, game);
        } else {
            var gameInstance = game.values().stream().findAny().orElse(null);
            if (gameInstance != null) {
                return (G) gameInstance;
            } else {
                return startGame(gameClass);
            }
        }
        return null;

    }
}

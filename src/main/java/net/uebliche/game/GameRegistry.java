package net.uebliche.game;

import net.uebliche.server.GameServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public final class GameRegistry {

    private static final List<Class<? extends Game<?>>> gameClazzs = new ArrayList<>();
    private static final HashMap<Class<? extends Game<?>>, HashMap<UUID, Object>> gameInstances = new HashMap<>();

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
        var map = gameInstances.getOrDefault(gameClazz, new HashMap<>());
        map.put(game.getId(), game);
        gamesById.put(game.getId(), game);
        return game;
    }

    public static <G extends Game<?>> G findGame(Class<G> gameClass) {
        if (!gameClazzs.contains(gameClass)) {
            log.error("Game class {} not registered", gameClass);
            throw new IllegalArgumentException("Game class not registered");
        }
        var game = gameInstances.get(gameClass);
        if (game == null) {
            game = new HashMap<>();
            gameInstances.put(gameClass, game);
        }
        var gameInstance = game.values().stream().findAny().orElse(null);
        if (gameInstance != null) {
            return (G) gameInstance;
        } else {
            try {
                return startGame(gameClass);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

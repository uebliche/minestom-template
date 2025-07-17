package net.uebliche.demo.game.survival;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.InstanceContainer;
import net.uebliche.demo.game.survival.commands.DisguiseCommand;
import net.uebliche.demo.game.survival.commands.HealCommand;
import net.uebliche.game.Game;
import net.uebliche.server.GamePlayer;
import net.uebliche.server.GameServer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Survival extends Game<SurvivalSettings> {

    private final InstanceContainer instanceContainer;

    public Survival(GameServer<GamePlayer> gameServer) {
        super(gameServer);
        instanceContainer = createInstance();
        commandManager.register(new HealCommand(this));
        commandManager.register(new DisguiseCommand(this));
    }

    @Override
    public void applySettings(SurvivalSettings settings) {

    }

    public InstanceContainer createInstance() {

        var propertiesFile = new File("worlds/survival/world.properties");
        Properties properties = new Properties();
        if (!propertiesFile.exists()) {
            properties.setProperty("seed", String.valueOf(ThreadLocalRandom.current().nextLong()));
            properties.setProperty("uuid", UUID.randomUUID().toString());
            properties.setProperty("spawn.x", "0.0");
            properties.setProperty("spawn.y", "64");
            properties.setProperty("spawn.z", "0.0");
            properties.setProperty("worldAge", "0");
        } else {
            try (FileReader reader = new FileReader(propertiesFile)) {
                properties.load(reader);
            } catch (IOException e) {
                log.error("Failed to load world properties", e);
                throw new RuntimeException(e);
            }
        }
        var container = new SurvivalContainer(UUID.fromString(properties.getProperty("uuid", UUID.randomUUID().toString())), Long.parseLong(properties.getProperty("seed", "0")));
        container.setWorldAge(Long.parseLong(properties.getProperty("worldAge", "0")));

        MinecraftServer.getSchedulerManager().buildShutdownTask(() -> {
            properties.setProperty("uuid", container.getUuid().toString());
            properties.setProperty("spawn.x", String.valueOf(container.getWorldSpawn().x()));
            properties.setProperty("spawn.y", String.valueOf(container.getWorldSpawn().x()));
            properties.setProperty("spawn.z", String.valueOf(container.getWorldSpawn().x()));
            properties.setProperty("worldAge", String.valueOf(container.getWorldAge()));
            try (FileWriter writer = new FileWriter(propertiesFile)) {
                properties.store(writer, "World Properties");
                log.info("World Properties saved to {}", propertiesFile.getAbsolutePath());
            } catch (IOException e) {
                log.error("Failed to save world properties", e);
            }
        });
        return container;
    }

    @Override
    public void onEnter(GamePlayer player) {
        player.setInstance(instanceContainer, new Pos(0, 100, 0));
    }

    @Override
    public void onLeave(GamePlayer player) {

    }

}

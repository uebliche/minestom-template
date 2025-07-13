package net.uebliche.server.abyss;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.instance.AddEntityToInstanceEvent;
import net.minestom.server.event.player.PlayerLoadedEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.tag.Tag;
import net.minestom.server.world.DimensionType;
import net.uebliche.server.GamePlayer;
import net.uebliche.server.GameServer;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Abyss<P extends GamePlayer> extends InstanceContainer {

    public Abyss(GameServer gameServer, BiConsumer<P, Boolean> onLoaded) {
        super(UUID.randomUUID(), DimensionType.THE_END);

        eventNode().addListener(AddEntityToInstanceEvent.class, event -> {
                    if (event.getEntity() instanceof Player player) {
                        player.setFlying(true);
                        player.setAllowFlying(true);
                        player.setGameMode(GameMode.SPECTATOR);
                        FakeLoadingScreen.load(player).thenAccept(unused -> {
                            onLoaded.accept((P) player, unused);
                        });
                    }
                })
                .addListener(PlayerMoveEvent.class, event -> {
                    event.setCancelled(true);
                })

        ;
        setChunkSupplier(LightingChunk::new);
        setGenerator(unit -> {
        });
        MinecraftServer.getInstanceManager().registerInstance(this);
    }

}

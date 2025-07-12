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

public class Abyss extends InstanceContainer {


    private static final Tag<FakeLoadingScreen> FAKE_LOADING_SCREEN_TAG = Tag.Transient("fake_loading_screen");

    public Abyss(GameServer gameServer) {
        super(UUID.randomUUID(), DimensionType.THE_END);

        eventNode().addListener(PlayerLoadedEvent.class, event -> {
                    //TODO: if banned etc. dont put them into the lobby.
                    event.getPlayer().getTag(FAKE_LOADING_SCREEN_TAG).setLoaded(true);
                })
                .addListener(AddEntityToInstanceEvent.class, event -> {
                    if (event.getEntity() instanceof Player player) {
                        player.setFlying(true);
                        player.setAllowFlying(true);
                        player.setGameMode(GameMode.SPECTATOR);
                        player.setTag(FAKE_LOADING_SCREEN_TAG, new FakeLoadingScreen(player));
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

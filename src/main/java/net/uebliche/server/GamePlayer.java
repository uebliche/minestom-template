package net.uebliche.server;

import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerSwapItemEvent;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.uebliche.server.mongodb.objects.user.User;
import org.jetbrains.annotations.NotNull;

public class GamePlayer extends Player {
    
    User user;
    
    public GamePlayer(@NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile, User user) {
        super(playerConnection, gameProfile);
        this.user = user;
        eventNode().addListener(PlayerSwapItemEvent.class, event -> {

        });
    }


}

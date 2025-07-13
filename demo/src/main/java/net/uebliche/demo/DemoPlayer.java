package net.uebliche.demo;

import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.uebliche.server.GamePlayer;
import net.uebliche.server.mongodb.objects.user.User;
import org.jetbrains.annotations.NotNull;

public class DemoPlayer extends GamePlayer {


    public DemoPlayer(@NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile, User user) {
        super(playerConnection, gameProfile, user);
    }
}

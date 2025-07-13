package net.uebliche.server;

import io.github.togar2.pvp.player.CombatPlayer;
import io.github.togar2.pvp.player.CombatPlayerImpl;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerSwapItemEvent;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.uebliche.game.Game;
import net.uebliche.mode.Mode;
import net.uebliche.server.mongodb.objects.user.User;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class GamePlayer extends CombatPlayerImpl {

    User user;
    Mode<?> mode;

    public GamePlayer(@NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile, User user) {
        super(playerConnection, gameProfile);
        this.user = user;
        eventNode().addListener(PlayerSwapItemEvent.class, event -> {

        });
    }

    @ApiStatus.Internal
    public void setMode(Mode<?> mode) {
        this.mode = mode;
    }

    public Mode<?> getMode() {
        return mode;
    }
}

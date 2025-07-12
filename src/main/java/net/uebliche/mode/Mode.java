package net.uebliche.mode;

import net.minestom.server.entity.Player;
import net.minestom.server.tag.Tag;
import net.uebliche.server.GamePlayer;
import net.uebliche.server.GameServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Mode<S extends ModeSettings> {

    public static final Tag PLAYER_MODE_TAG = Tag.Transient("player-mode");

    protected final Logger log;
    protected final UUID id = UUID.randomUUID();
    private final GameServer gameServer;
    private List<GamePlayer> players = new ArrayList<>();


    public Mode(GameServer gameServer) {
        this.log = LoggerFactory.getLogger(getClass());
        this.gameServer = gameServer;
    }

    public GameServer getGameServer() {
        return gameServer;
    }

    public UUID getId() {
        return id;
    }

    public final void enter(GamePlayer player) {
        if(player.getTag(PLAYER_MODE_TAG) != null){
            var oldMode = player.getTag(PLAYER_MODE_TAG);
            if(oldMode != this){
                leave(player);
            }else{
                return;
            }
        }
        players.add(player);
        player.setTag(PLAYER_MODE_TAG, this);
        onEnter(player);
    }
    public abstract void onEnter(GamePlayer player);

    public final void leave(GamePlayer player) {
        players.remove(player);
        player.setTag(PLAYER_MODE_TAG, null);
        onLeave(player);
    }
    public abstract void onLeave(GamePlayer player);

}

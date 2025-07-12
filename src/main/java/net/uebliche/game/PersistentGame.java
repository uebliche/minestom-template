package net.uebliche.game;

import net.uebliche.server.GameServer;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

public abstract class PersistentGame<Settings extends PresistentGameSettings> extends Game<Settings> {

    public PersistentGame(GameServer gameServer) {
        super(gameServer);
    }

    @Override
    public void applySettings(Settings settings) {

    }
}

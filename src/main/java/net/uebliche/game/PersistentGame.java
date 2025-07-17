package net.uebliche.game;

import net.uebliche.server.GameServer;

public abstract class PersistentGame<Settings extends PersistentGameSettings> extends Game<Settings> {

    public PersistentGame(GameServer gameServer) {
        super(gameServer);
    }

    @Override
    public void applySettings(Settings settings) {

    }
}

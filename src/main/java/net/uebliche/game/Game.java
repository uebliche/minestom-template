package net.uebliche.game;

import net.uebliche.mode.Mode;
import net.uebliche.server.GameServer;

public abstract class Game<Settings extends GameSettings> extends Mode<Settings> {

    public Game(GameServer gameServer) {
        super(gameServer);
    }
    public abstract void applySettings(Settings settings);
}

package net.uebliche.game;

import net.uebliche.server.GameServer;

public abstract class TimedGame extends Game<TimedGameSettings> {
    public TimedGame(GameServer gameServer) {
        super(gameServer);
    }

    @Override
    public void applySettings(TimedGameSettings settings) {
        
    }
}

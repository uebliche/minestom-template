package net.uebliche.game;

import net.uebliche.server.GameServer;

public abstract class EventGame extends Game<EventGameSettings> {

    public EventGame(GameServer gameServer) {
        super(gameServer);
    }

    @Override
    public void applySettings(EventGameSettings settings) {

    }
}

package net.uebliche.game;

import net.uebliche.server.GameServer;

public abstract class SandboxGame<Settings extends SandboxGameSettings> extends Game<Settings>{

    public SandboxGame(GameServer gameServer) {
        super(gameServer);
    }

    @Override
    public void applySettings(Settings settings) {

    }
}

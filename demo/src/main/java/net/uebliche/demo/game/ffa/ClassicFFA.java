package net.uebliche.demo.game.ffa;

import net.uebliche.game.PersistentGame;
import net.uebliche.server.GamePlayer;
import net.uebliche.server.GameServer;

public class ClassicFFA extends PersistentGame<ClassicFFASettings> {

    public ClassicFFA(GameServer gameServer) {
        super(gameServer);
    }

    @Override
    public void onEnter(GamePlayer player) {

    }

    @Override
    public void onLeave(GamePlayer player) {

    }
}

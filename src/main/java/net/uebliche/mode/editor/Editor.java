package net.uebliche.mode.editor;

import net.minestom.server.instance.InstanceContainer;
import net.uebliche.mode.Mode;
import net.uebliche.server.GamePlayer;
import net.uebliche.server.GameServer;

public class Editor extends Mode<EditorSettings> {


    public Editor(GameServer gameServer) {
        super(gameServer);
    }

    @Override
    public void onEnter(GamePlayer player) {

    }

    @Override
    public void onLeave(GamePlayer player) {

    }
}

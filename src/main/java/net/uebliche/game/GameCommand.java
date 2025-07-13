package net.uebliche.game;

import net.minestom.server.command.builder.Command;
import net.uebliche.server.GamePlayer;
import org.jetbrains.annotations.NotNull;

public abstract class GameCommand<G extends Game<?>> extends Command {

    public GameCommand(G game, @NotNull String name) {
        super(name);
        setCondition((sender, commandString) -> {
            if(!(sender instanceof GamePlayer gameplayer))
                return false;
            if(gameplayer.getMode().equals(game))
                return true;
            return false;
        });
    }
}

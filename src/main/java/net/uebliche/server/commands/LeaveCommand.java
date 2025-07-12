package net.uebliche.server.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.Player;
import net.uebliche.game.Game;
import net.uebliche.mode.Mode;
import net.uebliche.mode.lobby.Lobby;
import net.uebliche.server.GamePlayer;
import net.uebliche.server.GameServer;

public class LeaveCommand extends Command {

    public LeaveCommand() {
        super("leave", "lobby");
        setCondition(Conditions::playerOnly);
        setCondition(Lobby::isNotInLobby);
        setDefaultExecutor((sender, context) -> {
            GamePlayer player = (GamePlayer) sender;
            GameServer.getInstance().getLobby().enter(player);
        });
    }

}

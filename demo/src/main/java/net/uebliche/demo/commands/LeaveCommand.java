package net.uebliche.demo.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.condition.Conditions;
import net.uebliche.demo.lobby.Lobby;
import net.uebliche.server.GamePlayer;

public class LeaveCommand extends Command {

    public LeaveCommand(Lobby lobby) {
        super("leave", "lobby");
        setCondition(Conditions::playerOnly);
        setCondition(Lobby::isNotInLobby);
        setDefaultExecutor((sender, context) -> {
            GamePlayer player = (GamePlayer) sender;
            lobby.enter(player);
        });
    }

}

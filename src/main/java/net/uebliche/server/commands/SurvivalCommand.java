package net.uebliche.server.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.condition.Conditions;
import net.uebliche.mode.lobby.Lobby;

public class SurvivalCommand extends Command {


    public SurvivalCommand() {
        super("survival");
        setCondition(Conditions::playerOnly);
        setCondition(Lobby::isInLobby);
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Sending into the Survival Mode");
        });
    }
}

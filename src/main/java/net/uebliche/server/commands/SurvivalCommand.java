package net.uebliche.server.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.condition.Conditions;

public class SurvivalCommand extends Command {


    public SurvivalCommand() {
        super("survival");
        setCondition(Conditions::playerOnly);
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Sending into the Survival Mode");
        });
    }
}

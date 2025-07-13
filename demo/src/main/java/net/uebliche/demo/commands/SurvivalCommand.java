package net.uebliche.demo.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.condition.Conditions;
import net.uebliche.demo.game.survival.Survival;
import net.uebliche.server.GamePlayer;

import java.util.function.Supplier;

public class SurvivalCommand extends Command {

    public SurvivalCommand(Supplier<Survival> survival) {
        super("survival");
        setCondition(Conditions::playerOnly);

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof GamePlayer player))
                return;
            if (player.getMode() instanceof Survival) {
                sender.sendMessage("You are already in the Survival Mode");
            } else {
                sender.sendMessage("Sending into the Survival Mode");
                survival.get().enter(player);
            }
        });
    }
}

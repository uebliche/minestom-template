package net.uebliche.demo.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.condition.Conditions;
import net.uebliche.demo.game.ffa.ClassicFFA;
import net.uebliche.demo.game.survival.Survival;
import net.uebliche.server.GamePlayer;

import java.util.function.Supplier;

public class ClassicFFACommand extends Command {

    public ClassicFFACommand(Supplier<ClassicFFA> ffa) {
        super("ffa");
        setCondition(Conditions::playerOnly);

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof GamePlayer player))
                return;
            if (player.getMode() instanceof Survival) {
                sender.sendMessage("You are already in the FFA Mode");
            } else {
                sender.sendMessage("Sending into the FFA Mode");
                ffa.get().enter(player);
            }
        });
    }
}

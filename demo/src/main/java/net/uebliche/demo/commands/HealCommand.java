package net.uebliche.demo.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.Player;
import net.uebliche.demo.game.survival.SurvivalContainer;

public class HealCommand extends Command {
    public HealCommand() {
        super("heal");
        setCondition(Conditions::playerOnly);
        setCondition(SurvivalContainer::isInContainer);
        addSyntax((sender, context) -> {
            if(sender instanceof Player player){
                player.heal();
            }
        });
    }
}

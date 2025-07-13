package net.uebliche.demo.game.survival.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.Player;
import net.uebliche.demo.game.survival.SurvivalContainer;
import net.uebliche.game.Game;
import net.uebliche.game.GameCommand;

public class HealCommand extends GameCommand {

    public HealCommand(Game game) {
        super(game, "heal");

        setCondition(Conditions::playerOnly);
        setCondition(SurvivalContainer::isInSurvival);

        addSyntax((sender, context) -> {
            if (sender instanceof Player player) {
                player.heal();
            }
        });
        setDefaultExecutor((sender, context) -> {
            if (sender instanceof Player player) {
                player.heal();
            }
        });
    }
}

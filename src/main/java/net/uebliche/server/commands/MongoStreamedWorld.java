package net.uebliche.server.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public class MongoStreamedWorld extends Command {

    public MongoStreamedWorld(Instance instance){
        super("mongostreamedworld");
        setCondition(Conditions::playerOnly);
        setDefaultExecutor((sender, context) -> {
            Player player = (Player) sender;
            player.setInstance(instance);
        });
    }

}

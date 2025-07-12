package net.uebliche.server.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;

import java.util.Locale;

public class GameModeCommand extends Command {
    
    public GameModeCommand() {
        super("gamemode");
        setCondition(Conditions::playerOnly);
        addSyntax((sender, context) -> {
            GameMode gameMode = context.get("gamemode");
            Player player = (Player) sender;
            player.setGameMode(gameMode);
            player.sendMessage(Component.translatable("commands.gamemode.success.self").arguments(Component.translatable("gameMode." + gameMode.name().toLowerCase(Locale.ROOT))));
        }, ArgumentType.Enum("gamemode", GameMode.class).setFormat(ArgumentEnum.Format.LOWER_CASED));
        addSyntax((commandSender, commandContext) -> {
            GameMode gameMode = commandContext.get("gamemode");
            var target = commandContext.get(ArgumentType.Entity("target")).findFirstPlayer(commandSender);
            if (target == null) {
                commandSender.sendMessage(Component.translatable("argument.entity.notfound.player"));
            } else {
                target.setGameMode(gameMode);
                commandSender.sendMessage(Component.translatable("commands.gamemode.success.other").arguments(target.getName(),
                Component.translatable("gameMode." + gameMode.name().toLowerCase(Locale.ROOT)), Component.text(target.getUsername())));
                target.sendMessage(Component.translatable("commands.gamemode.success.self").arguments(Component.translatable("gameMode." + gameMode.name().toLowerCase(Locale.ROOT))));
            }
        }, ArgumentType.Enum("gamemode", GameMode.class).setFormat(ArgumentEnum.Format.LOWER_CASED), ArgumentType.Entity("target"));
    }
}

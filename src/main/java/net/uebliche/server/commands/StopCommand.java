package net.uebliche.server.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;

public class StopCommand extends Command {
    
    public StopCommand() {
        super("stop");
        setDefaultExecutor((commandSender, commandContext) -> {
            MinecraftServer.getInstanceManager().getInstances().forEach(instance -> {
                instance.saveChunksToStorage().thenAccept(unused -> {
                   commandSender.sendMessage(instance.getUuid() + " saved.");
                }).join();
            });
            MinecraftServer.stopCleanly();
        });
    }
}

package net.uebliche.server.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.utils.mojang.MojangUtils;
import net.uebliche.server.GameServer;
import net.uebliche.server.mongodb.objects.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public class BanCommand extends Command {

    private static final Logger log = LoggerFactory.getLogger(BanCommand.class);

    public BanCommand() {
        super("ban");
        var target = ArgumentType.Word("target").from(GameServer
                .getInstance()
                .getUserRepository()
                .findAll()
                .thenApply(userStream -> userStream.map(User::getName)).join().toArray(String[]::new));


        addSyntax((sender, context) -> {
            sender.sendMessage("Banned " + context.get("target"));
            UUID user = null;
            try {
                user = MojangUtils.getUUID(context.get("target"));
            } catch (IOException e) {
                log.error("Failed to get UUID for " + context.get("target"), e);
            }
            if (user == null) {
                sender.sendMessage("User not found");
                return;
            }
            GameServer.getInstance().getUserRepository().findByUUID(user).thenAccept(user1 -> {
                GameServer.getInstance().getBanRepository();
            });
        }, target);

    }
}

package net.uebliche.demo.game.survival.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.other.FallingBlockMeta;
import net.minestom.server.instance.block.Block;
import net.uebliche.demo.game.survival.Survival;
import net.uebliche.demo.game.survival.SurvivalContainer;
import net.uebliche.game.GameCommand;
import org.jetbrains.annotations.NotNull;

public class DisguiseCommand extends GameCommand<Survival> {

    public DisguiseCommand(Survival survival) {
        super(survival, "disguise");

        setCondition(Conditions::playerOnly);
        setCondition(SurvivalContainer::isInSurvival);
        super.addSyntax(this::testDisguisement);

    }

    private void testDisguisement(@NotNull CommandSender sender, @NotNull CommandContext ctx) {
        if (sender instanceof Player player) {
            player.switchEntityType(EntityType.FALLING_BLOCK);
            player.editEntityMeta(FallingBlockMeta.class, meta -> meta.setBlock(Block.ANVIL));
            player.getViewers().forEach(player::updateNewViewer);
            sender.sendMessage(Component.translatable("commands.disguise.selfApplied"));
        } else {
            sender.sendMessage(Component.translatable("commands.playerOnly"));
        }
    }
}

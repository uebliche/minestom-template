package net.uebliche.utils.item;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.instance.block.Block;

import java.util.Optional;

public record Action(
        Optional<Entity> targetEntity,
        Optional<Block> targetBlock,
        Optional<PlayerHand> hand,
        Optional<EquipmentSlot> equipmentSlot,
        BooleanConsumer cancel
) {

}
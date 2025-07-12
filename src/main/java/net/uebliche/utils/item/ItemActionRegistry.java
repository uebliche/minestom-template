package net.uebliche.utils.item;

import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PlayerCancelItemUseEvent;
import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.event.trait.InstanceEvent;

import java.util.UUID;


public final class ItemActionRegistry {


    public static EventNode<InstanceEvent> eventNode() {
        return EventNode.type("ItemActionRegistry-" + UUID.randomUUID(), EventFilter.INSTANCE)

                .addListener(ItemDropEvent.class, event -> Item.fromItemStack(event.getItemStack()).ifPresent(item ->
                        item.call(Item.Action.drop(event.getPlayer(), event::setCancelled))))
                .addListener(PlayerUseItemOnBlockEvent.class, event -> Item.fromItemStack(event.getItemStack()).ifPresent(item ->
                        item.call(Item.Action.block(event.getPlayer(), new Item.BlockAction(event.getBlockFace(),
                                event.getPosition())))))
                .addListener(PlayerUseItemEvent.class, event -> Item.fromItemStack(event.getItemStack()).ifPresent(item ->
                        item.call(Item.Action.using(event.getPlayer(), event.getHand(), event.getItemUseTime()))))
                .addListener(PlayerFinishItemUseEvent.class, event -> Item.fromItemStack(event.getItemStack()).ifPresent(item ->
                        item.call(Item.Action.finishUsing(event.getPlayer(), event.getHand(), event.getUseDuration()))))
                .addListener(PlayerCancelItemUseEvent.class, event -> Item.fromItemStack(event.getItemStack()).ifPresent(item ->
                        item.call(Item.Action.cancelUsing(event.getPlayer(), event.getHand(), event.getUseDuration()))))
                ;
    }
}

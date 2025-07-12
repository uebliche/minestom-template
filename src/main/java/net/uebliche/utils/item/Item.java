package net.uebliche.utils.item;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EquipmentSlotGroup;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.attribute.AttributeModifier;
import net.minestom.server.entity.attribute.AttributeOperation;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.AttributeList;
import net.minestom.server.tag.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;

public class Item {

    private static final Tag<UUID> TAG_ITEM = Tag.UUID("item-uuid");
    private static final HashMap<UUID, Item> itemMap = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(Item.class);

    private UUID uuid = UUID.randomUUID();
    private Material material;
    private Component itemName;
    private List<Equipment> equipments;
    private List<AttributeList.Modifier> attributeModifiers;
    private List<Consumer<Action>> itemActions;

    private Item(Builder builder) {
        itemMap.put(uuid, this);
        this.itemName = builder.itemName;
        this.material = builder.material;
        this.equipments = builder.equipments;
        this.attributeModifiers = builder.attributeModifiers;
        this.itemActions = builder.itemActions;
    }

    public static Optional<Item> fromItemStack(ItemStack itemStack) {
        return Optional.ofNullable(itemMap.get(itemStack.getTag(TAG_ITEM)));
    }

    protected void call(Action call) {
        for (var action : itemActions) {
            action.accept(call);
        }
    }

    public ItemStack toItemStack() {
        var builder = ItemStack.builder(material);
        builder.setTag(TAG_ITEM, uuid);
//        for (var equipment : equipments) {
//            builder.set(DataComponents.EQUIPPABLE, new Equippable(
//                    equipment.getEquipmentSlot(),
//                    equipment.getEquipSound(),
//                    null,
//                    equipment.camaraOverlay(),
//                    null,
//                    equipment.isDispensable(),
//                    equipment.isSwappable(),
//                    equipment.damageOnHurt(),
//                    equipment.equipOnInteract(),
//                    equipment.canBeSheared(),
//                    equipment.shearingSound()
//            ));
//        }
        if (itemName != null)
            builder.set(DataComponents.ITEM_NAME, itemName);
        for (var modifier : attributeModifiers) {
            builder.set(DataComponents.ATTRIBUTE_MODIFIERS, new AttributeList(List.of(modifier)));
        }
        builder.set(DataComponents.ATTRIBUTE_MODIFIERS, new AttributeList(attributeModifiers));

        return builder.build();
    }


    public record Action(
            Player player,
            Optional<BlockAction> block,
            Optional<EntityAction> entity,
            Optional<UseAction> use,
            Optional<DropAction> drop
    ) {
        public static Action block(Player player, BlockAction blockAction) {
            return new Action(player, Optional.of(blockAction), Optional.empty(), Optional.empty(), Optional.empty());
        }

        public static Action entity(Player player, EntityAction entityAction) {
            return new Action(player, Optional.empty(), Optional.of(entityAction), Optional.empty(), Optional.empty());
        }

        public static Action using(Player player, PlayerHand playerHand, long itemUseTime) {
            return new Action(player, Optional.empty(), Optional.empty(), Optional.of(UseAction.Using(playerHand, itemUseTime)), Optional.empty());
        }

        public static Action finishUsing(Player player, PlayerHand playerHand, long useDuration) {
            return new Action(player, Optional.empty(), Optional.empty(), Optional.of(UseAction.Finished(playerHand,
                    useDuration)), Optional.empty());

        }

        public static Action cancelUsing(Player player, PlayerHand playerHand, long useDuration) {
            return new Action(player, Optional.empty(), Optional.empty(), Optional.of(UseAction.Cancelled(playerHand,
                    useDuration)), Optional.empty());
        }

        public static Action drop(Player player, Consumer<Boolean> cancel) {
            return new Action(player, Optional.empty(), Optional.empty(), Optional.empty(),
                    Optional.of(new DropAction(cancel)));
        }
    }

    public static record UseAction(
            PlayerHand hand,
            State state,
            long useTime,
            long useDuration
    ) {

        static UseAction Finished(PlayerHand hand, long useDuration) {
            return new UseAction(hand, State.FINISHED, -1, useDuration);
        }

        static UseAction Cancelled(PlayerHand hand, long useDuration) {
            return new UseAction(hand, State.CANCELLED, -1, useDuration);
        }

        static UseAction Using(PlayerHand hand, long itemUseTime) {
            return new UseAction(hand, State.USING, itemUseTime, -1);
        }
    }

    public static enum State {
        FINISHED,
        CANCELLED,
        USING;
    }

    public static record BlockAction(
            BlockFace blockFace,
            Point blockPosition
    ) {
    }

    public static record EntityAction(
            Entity targetEntity
    ) {
    }

    public static record DropAction(
            Consumer<Boolean> cancel
    ) {
    }


    public static class Builder {

        private Material material;
        private Component itemName;
        private List<Equipment> equipments = new ArrayList<>();
        private List<AttributeList.Modifier> attributeModifiers = new ArrayList<>();
        private List<Consumer<Action>> itemActions = new ArrayList<>();

        public Builder(Material material) {
            this.material = material;
        }

        public Builder withEquipment(Equipment equipment) {
            equipments.add(equipment);
            return this;
        }

        public Builder withItemName(Component itemName) {
            this.itemName = itemName;
            return this;
        }

        public Builder withAttributeModifier(Attribute attribute, double amount, AttributeOperation operation, EquipmentSlotGroup group) {
            attributeModifiers.add(
                    new AttributeList.Modifier(
                            attribute,
                            new AttributeModifier(
                                    UUID.randomUUID().toString(),
                                    amount,
                                    operation
                            ),
                            group
                    )
            );
            return this;
        }

        public Builder withActionHandler(Consumer<Action> actionHandler) {
            itemActions.add(actionHandler);
            return this;
        }


        public Item build() {
            return new Item(this);
        }

    }


}

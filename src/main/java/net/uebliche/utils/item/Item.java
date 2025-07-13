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

import java.time.Duration;
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
            Optional<DropAction> drop,
            Optional<Consumer<Boolean>> cancel
    ) {

        public static class Builder {
            private Player player;
            private BlockAction blockAction;
            private EntityAction entityAction;
            private UseAction useAction;
            private DropAction dropAction;
            private Consumer<Boolean> cancel;

            public Builder(Player player) {
                this.player = player;
            }

            public Builder block(BlockAction blockAction) {
                this.blockAction = blockAction;
                return this;
            }

            public Builder entity(EntityAction entityAction) {
                this.entityAction = entityAction;
                return this;
            }

            public Builder use(UseAction useAction) {
                this.useAction = useAction;
                return this;
            }

            public Builder drop(DropAction dropAction) {
                this.dropAction = dropAction;
                return this;
            }

            public Builder cancel(Consumer<Boolean> cancel) {
                this.cancel = cancel;
                return this;
            }

            public Action build() {
                return new Action(
                        player,
                        Optional.ofNullable(blockAction),
                        Optional.ofNullable(entityAction),
                        Optional.ofNullable(useAction),
                        Optional.ofNullable(dropAction),
                        Optional.ofNullable(cancel)
                );
            }

        }

        public static Action block(Player player, BlockAction blockAction) {
            return new Action.Builder(player).block(blockAction).build();
        }

        public static Action entity(Player player, EntityAction entityAction, Consumer<Boolean> cancel) {
            return new Action.Builder(player).entity(entityAction).cancel(cancel).build();
        }

        public static Action using(Player player, PlayerHand playerHand, long itemUseTime, Consumer<Boolean> cancel,
                                   Consumer<Long> setItemUseTime) {
            return new Action.Builder(player).use(UseAction.Using(playerHand, itemUseTime, setItemUseTime)).cancel(cancel).build();

        }

        public static Action finishUsing(Player player, PlayerHand playerHand, long useDuration, Consumer<Boolean> startRiptide) {
            return new Builder(player).use(UseAction.Finished(playerHand, useDuration)).build();
        }

        public static Action cancelUsing(Player player, PlayerHand playerHand, long useDuration,
                                         Consumer<Boolean> startRiptide) {
            return new Builder(player).use(UseAction.Cancelled(playerHand, useDuration)).build();
        }

        public static Action drop(Player player, Consumer<Boolean> cancel) {
            return new Builder(player).drop(new DropAction()).cancel(cancel).build();
        }

        public void applyCooldown(Duration duration) {
            //TODO: implement
        }

        public void setCancel(boolean cancel) {

        }
    }

    public static record UseAction(
            PlayerHand hand,
            State state,
            long useTime,
            long useDuration,
            Consumer<Boolean> startRiptide,
            Consumer<Long> setItemUseTime
    ) {

        static UseAction Finished(PlayerHand hand, long useDuration) {
            return new UseAction(hand, State.FINISHED, -1, useDuration, null, null);
        }

        static UseAction Cancelled(PlayerHand hand, long useDuration) {
            return new UseAction(hand, State.CANCELLED, -1, useDuration, null, null);
        }

        static UseAction Using(PlayerHand hand, long itemUseTime, Consumer<Long> setItemUseTime) {
            return new UseAction(hand, State.USING, itemUseTime, -1, null, setItemUseTime);
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

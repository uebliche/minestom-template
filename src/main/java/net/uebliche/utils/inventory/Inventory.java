package net.uebliche.utils.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import net.uebliche.server.GamePlayer;
import net.uebliche.utils.item.ItemTags;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class Inventory extends net.minestom.server.inventory.Inventory {

    private final HashMap<UUID, Consumer<Callback>> clickListeners = new HashMap<>();
    private static final Tag<UUID> ITEM_CLICK_LISTENER = Tag.UUID("item_click_listener");

    public Inventory(@NotNull InventoryType inventoryType, @NotNull Component title) {
        super(inventoryType, title);
        eventNode()
                .addListener(InventoryClickEvent.class, event -> {
                })
                .addListener(InventoryCloseEvent.class, event -> {
                    clickListeners.clear();
                })
                .addListener(InventoryPreClickEvent.class, event -> {
                    var item = event.getClickedItem();
                    Optional.ofNullable(item.getTag(ITEM_CLICK_LISTENER)).flatMap(id -> Optional.ofNullable(clickListeners.get(id))).ifPresent(consumer -> consumer.accept(new Callback((GamePlayer) event.getPlayer(), event.getClick(), event.getSlot())));
                    event.setCancelled(true);
                })
                .addListener(InventoryOpenEvent.class, event -> {
                    generate();
                })
        ;
    }

    public abstract void generate();

    public void addClickableItemStack(ItemStack itemStack, Consumer<Callback> onClick) {
        var id = UUID.randomUUID();
        addItemStack(itemStack.withTag(ITEM_CLICK_LISTENER, id));
        clickListeners.put(id, onClick);
    }

    public void setClickableItemStack(int slot, ItemStack itemStack, Consumer<Callback> onClick) {
        var id = UUID.randomUUID();
        setItemStack(slot, itemStack.withTag(ITEM_CLICK_LISTENER, id));
        clickListeners.put(id, onClick);
    }

    public record Callback(GamePlayer player, Click click, Integer slot) {

        public boolean isLeft() {
            return click instanceof Click.LeftShift || click instanceof Click.Left;
        }

        public boolean isRight() {
            return click instanceof Click.RightShift || click instanceof Click.Right;
        }

        public boolean isMiddle() {
            return click instanceof Click.Middle;
        }

        public boolean isShift() {
            return click instanceof Click.LeftShift || click instanceof Click.RightShift;
        }

        public boolean isDrag() {
            return click instanceof Click.Drag;
        }

        public boolean isDrop() {
            return click instanceof Click.DropSlot || click instanceof Click.DropCursor;
        }


    }

}

package net.uebliche.demo.lobby.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.uebliche.demo.game.ffa.ClassicFFA;
import net.uebliche.demo.game.survival.Survival;
import net.uebliche.game.GameRegistry;
import net.uebliche.server.GameServer;
import net.uebliche.utils.inventory.Inventory;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class NavigatorInventory extends Inventory {

    public NavigatorInventory() {
        super(InventoryType.CHEST_6_ROW, Component.text("Navigator"));
    }

    @Override
    public void generate() {
        addClickableItemStack(ItemStack.builder(Material.DIAMOND).lore(Component.text("Left Shift"), Component.text(
                "Right Shift"), Component.text("Middle Click (Creative only)"), Component.text("DROP")).build(),
                click -> {
            if (click.isLeft() && click.isShift())
                click.player().sendMessage("Left Shift on Diamond!");
            if(click.isRight())
                click.player().sendMessage("Right Click on Diamond!");
            if(click.isMiddle())
                click.player().sendMessage("Middle Click on Diamond!");
            if(click.isDrop())
                click.player().sendMessage("Drop on Diamond!");
        });
        setClickableItemStack(5, ItemStack.builder(Material.GOLD_INGOT).build(), click -> {
            click.player().sendMessage("Clicked on Gold Ingot!");
            try {
                Objects.requireNonNull(GameRegistry.findGame(ClassicFFA.class)).enter(click.player());
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        setClickableItemStack(18, ItemStack.builder(Material.IRON_INGOT).build(), click -> {
            new Survival(GameServer.getInstance()).enter(click.player());
            try {
                GameRegistry.startGame(Survival.class).enter(click.player());
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

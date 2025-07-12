package net.uebliche.mode.lobby.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.uebliche.game.survival.Survival;
import net.uebliche.server.GameServer;
import net.uebliche.utils.inventory.Inventory;

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
        });

        setClickableItemStack(18, ItemStack.builder(Material.IRON_INGOT).build(), click -> {
            new Survival(GameServer.getInstance()).enter(click.player());
///            GameRegistry.startGame(Survival.class).enter(click.player());
        });
    }
}

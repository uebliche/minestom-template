package net.uebliche.demo.game.ffa;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.uebliche.game.PersistentGame;
import net.uebliche.server.GamePlayer;
import net.uebliche.server.GameServer;
import net.uebliche.utils.item.Item;

public class ClassicFFA extends PersistentGame<ClassicFFASettings> {

    InstanceContainer instanceContainer;

    public ClassicFFA(GameServer gameServer) {
        super(gameServer);
        instanceContainer = MinecraftServer.getInstanceManager().createInstanceContainer();
        instanceContainer.setChunkLoader(new AnvilLoader("worlds/ffa"));
        instanceContainer.setGenerator(unit -> {
            unit.modifier().fillHeight(0, 1, Block.BEDROCK);
            unit.modifier().fillHeight(1, 4, Block.DIRT);
            unit.modifier().fillHeight(4, 5, Block.GRASS_BLOCK);
        });
        MinecraftServer.getInstanceManager().registerInstance(instanceContainer);
    }


    @Override
    public void onEnter(GamePlayer player) {
        player.setFlying(false);
        player.setGameMode(GameMode.ADVENTURE);
        player.setInstance(instanceContainer, new Pos(0, 10, 0));
        player.getInventory().addItemStack(ItemStack.builder(Material.DIAMOND_SWORD).build());
        player.setHelmet(ItemStack.builder(Material.DIAMOND_HELMET).build());
        player.setChestplate(ItemStack.builder(Material.DIAMOND_CHESTPLATE).build());
        player.setLeggings(ItemStack.builder(Material.DIAMOND_LEGGINGS).build());
        player.setBoots(ItemStack.builder(Material.DIAMOND_BOOTS).build());
    }

    @Override
    public void onLeave(GamePlayer player) {
        player.getInventory().clear();
    }
}

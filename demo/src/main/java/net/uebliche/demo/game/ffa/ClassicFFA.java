package net.uebliche.demo.game.ffa;

import io.github.togar2.pvp.feature.CombatFeatureSet;
import io.github.togar2.pvp.feature.CombatFeatures;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.uebliche.game.PersistentGame;
import net.uebliche.server.GamePlayer;
import net.uebliche.server.GameServer;
import net.uebliche.utils.item.Item;
import net.uebliche.utils.item.ItemActionRegistry;

import java.time.Duration;

public class ClassicFFA extends PersistentGame<ClassicFFASettings> {

    InstanceContainer instanceContainer;

    private Item foodItem;

    public ClassicFFA(GameServer gameServer) {
        super(gameServer);
        CombatFeatureSet legacy = CombatFeatures.legacyVanilla();

        instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setChunkLoader(new AnvilLoader("worlds/ffa"));
        instanceContainer.setGenerator(unit -> {
            unit.modifier().fillHeight(0, 1, Block.BEDROCK);
            unit.modifier().fillHeight(1, 4, Block.DIRT);
            unit.modifier().fillHeight(4, 5, Block.GRASS_BLOCK);
        });
        instanceContainer.setChunkSupplier(LightingChunk::new);

        instanceContainer.eventNode()
                .addChild(ItemActionRegistry.eventNode())
                .addChild(legacy.createNode());

        instanceManager.registerInstance(instanceContainer);

        foodItem = new Item.Builder(Material.BEETROOT_SOUP)
                .withActionHandler(action -> {
                    action.setCancel(true);
                    action.use().ifPresent(useAction -> {
                        if (useAction.hand() == PlayerHand.OFF)
                            return;
                        action.player().setFood(20);
                        action.player().setFoodSaturation(10F);
                        action.applyCooldown(Duration.ofSeconds(10));
                    });
                }).build();
    }

    @Override
    public void onEnter(GamePlayer player) {
        player.setGameMode(GameMode.ADVENTURE);

        player.setInstance(instanceContainer, new Pos(0, 10, 0)).thenAccept(unused -> {
            player.addEffect(new Potion(PotionEffect.SLOW_FALLING, 1, 20*10));
        });
        player.getInventory().addItemStack(ItemStack.builder(Material.DIAMOND_SWORD).build());
        player.setHelmet(ItemStack.builder(Material.DIAMOND_HELMET).build());
        player.setChestplate(ItemStack.builder(Material.DIAMOND_CHESTPLATE).build());
        player.setLeggings(ItemStack.builder(Material.DIAMOND_LEGGINGS).build());
        player.setBoots(ItemStack.builder(Material.DIAMOND_BOOTS).build());

        player.getInventory().addItemStack(foodItem.toItemStack());
    }

    @Override
    public void onLeave(GamePlayer player) {
        player.getInventory().clear();
    }
}

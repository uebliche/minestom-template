package net.uebliche.demo.lobby;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.EquipmentSlotGroup;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.attribute.AttributeOperation;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.entity.EntityDamageEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.sound.SoundEvent;
import net.uebliche.demo.DemoServer;
import net.uebliche.demo.commands.ClassicFFACommand;
import net.uebliche.demo.lobby.inventory.NavigatorInventory;
import net.uebliche.mode.Mode;
import net.uebliche.mode.ModeSettings;
import net.uebliche.server.GamePlayer;
import net.uebliche.utils.item.Equipment;
import net.uebliche.utils.item.Item;
import net.uebliche.utils.item.ItemActionRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Lobby extends Mode<ModeSettings> {

    InstanceContainer instanceContainer;

    public Lobby(DemoServer gameServer) {
        super(gameServer);
        instanceContainer = createInstance();
        instanceContainer.eventNode().addChild(ItemActionRegistry.eventNode());
        //new ZombieCreature().setInstance(instanceContainer, new Pos(0, 5, 0));
    }

    Item navigator = new Item.Builder(Material.COMPASS)
            .withEquipment(new Equipment() {
                @Override
                public EquipmentSlot getEquipmentSlot() {
                    return EquipmentSlot.BODY;
                }

                @Override
                public SoundEvent getEquipSound() {
                    return SoundEvent.ITEM_ARMOR_EQUIP_LEATHER;
                }
            })
            .withAttributeModifier(Attribute.ARMOR_TOUGHNESS, 2, AttributeOperation.ADD_VALUE,
                    EquipmentSlotGroup.OFF_HAND)
            .withAttributeModifier(Attribute.SCALE, 2, AttributeOperation.ADD_VALUE,
                    EquipmentSlotGroup.MAIN_HAND)
            .withActionHandler(action -> {
                action.drop().ifPresent(drop -> {
                    action.setCancel(true);
                });
                action.block().ifPresent(block -> {

                });
                action.use().ifPresent(useAction -> {
                    if (useAction.hand() == PlayerHand.MAIN) {
                        action.player().sendMessage("You used the compass!");
                        action.player().openInventory(new NavigatorInventory());
                    }
                });
            })
            .build();
    Item testItem = new Item.Builder(Material.DIAMOND)
            .build();

    public InstanceContainer createInstance() {
        var container = MinecraftServer.getInstanceManager().createInstanceContainer();
        container.setChunkLoader(new AnvilLoader("worlds/world"));
        container.setGenerator(unit -> {
            unit.modifier().fillHeight(0, 1, Block.BEDROCK);
            unit.modifier().fillHeight(1, 4, Block.DIRT);
            unit.modifier().fillHeight(4, 5, Block.GRASS_BLOCK);
        });
        container.setChunkSupplier(LightingChunk::new);
        MinecraftServer.getSchedulerManager().buildShutdownTask(container::saveChunksToStorage);
        container.eventNode()
                .addListener(EntityDamageEvent.class, event -> {
                    if (event.getEntity() instanceof GamePlayer player) {
                        if (Objects.equals(event.getDamage().getType().resolve(MinecraftServer.getDamageTypeRegistry()), DamageType.OUT_OF_WORLD.resolve(MinecraftServer.getDamageTypeRegistry()))) {
                            player.teleport(player.getRespawnPoint());
                            event.setCancelled(true);
                        }
                    }
                })
                .addListener(PlayerSpawnEvent.class, event -> {
                    event.getPlayer().sendMessage("Spawn on Anvil Instance");
                    event.getPlayer().sendMessage("Respawn Point: " + event.getPlayer().getRespawnPoint());
                    if (event.getPlayer().getRespawnPoint().distance(Pos.ZERO) == 0) {
                        event.getPlayer().setRespawnPoint(new Pos(ThreadLocalRandom.current().nextInt(-10, 10), 5, ThreadLocalRandom.current().nextInt(-10, 10)));
                    }
                })
        ;
        return container;
    }

    @Override
    public void onEnter(GamePlayer player) {
        player.setFlying(true);
        player.setAllowFlying(true);
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.setInstance(instanceContainer, new Pos(0, 5, 0)).thenAccept(unused -> {
            player.getInventory().setItemStack(4, navigator.toItemStack());
            player.getInventory().setItemStack(5, testItem.toItemStack());
        });

    }

    @Override
    public void onLeave(GamePlayer player) {
        player.getInventory().clear();

    }


    public static boolean isNotInLobby(@NotNull CommandSender commandSender, @Nullable String s) {
        if (!(commandSender instanceof GamePlayer player)) return false;
        return !(player.getTag(PLAYER_MODE_TAG) instanceof Lobby);
    }

    public static boolean isInLobby(@NotNull CommandSender commandSender, @Nullable String s) {
        if (!(commandSender instanceof GamePlayer player)) return false;
        return (player.getTag(PLAYER_MODE_TAG) instanceof Lobby);
    }
}

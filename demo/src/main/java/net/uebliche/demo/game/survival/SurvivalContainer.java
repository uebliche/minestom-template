package net.uebliche.demo.game.survival;

import com.dfsek.terra.minestom.world.TerraMinestomWorld;
import com.dfsek.terra.minestom.world.TerraMinestomWorldBuilder;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.entity.EntityDamageEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.time.TimeUnit;
import net.minestom.server.world.DimensionType;
import net.uebliche.server.GamePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class SurvivalContainer extends InstanceContainer {

    private Pos worldSpawn = new Pos(0, 100, 0);

    public SurvivalContainer(UUID uuid, Long seed) {
        super(uuid, DimensionType.OVERWORLD);
        MinecraftServer.getInstanceManager().registerInstance(this);
        setGenerator(unit -> {
        });
        setChunkLoader(new AnvilLoader("worlds/survival"));
        TerraMinestomWorld world = TerraMinestomWorldBuilder.from(this).defaultPack().seed(seed).attach();
        this.setChunkSupplier(LightingChunk::new);

        MinecraftServer.getSchedulerManager().buildShutdownTask(this::saveChunksToStorage);
        eventNode()
                .addListener(EntityDamageEvent.class, event -> {
                })
                .addListener(PlayerSpawnEvent.class, event -> {
                    event.getPlayer().sendMessage("Spawn on Survival Instance");
                    event.getPlayer().teleport(new Pos(0, 100, 0));
                    event.getPlayer().setRespawnPoint(new Pos(0, 100, 0));
                }).addListener(PlayerBlockBreakEvent.class, event -> {
                    Optional.ofNullable(event.getBlock().registry().material()).ifPresent(material -> {
                        var item = new ItemEntity(ItemStack.of(material));
                        item.setInstance(this, event.getBlockPosition().add(0.5, 0.5, 0.5));
                        item.setVelocity(new Vec(ThreadLocalRandom.current().nextDouble(-1, 1), ThreadLocalRandom.current().nextDouble(0.5, 1.3), ThreadLocalRandom.current().nextDouble(-1, 1)));
                    });
                }).addListener(PickupItemEvent.class, event -> {
                    if (event.getEntity() instanceof Player player) {

                        if (!player.getInventory().addItemStack(event.getItemStack())) event.setCancelled(true);

                    } else event.setCancelled(true);
                }).addListener(ItemDropEvent.class, event -> {
                    Player player = event.getEntity();
                    var playerPos = player.getPosition();
                    var itemEntity = new ItemEntity(event.getItemStack());
                    itemEntity.setPickupDelay(Duration.of(500, TimeUnit.MILLISECOND));
                    itemEntity.setInstance(player.getInstance(), playerPos.withY(y -> y + 1.5));
                    Vec velocity = playerPos.direction().mul(6);
                    itemEntity.setVelocity(velocity);
                });
    }

    public void setWorldSpawn(Pos worldSpawn) {
        this.worldSpawn = worldSpawn;
    }

    public Pos getWorldSpawn() {
        return worldSpawn;
    }

    public static boolean isInSurvival(@NotNull CommandSender commandSender, @Nullable String s) {
        if (!(commandSender instanceof GamePlayer player)) return false;
        if (player.getMode() instanceof Survival survival) return true;
        return false;
    }
}

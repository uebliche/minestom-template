package net.uebliche.mode;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.AttributeInstance;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import net.uebliche.server.GamePlayer;
import net.uebliche.server.GameServer;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Mode<S extends ModeSettings> {

    public static final Tag PLAYER_MODE_TAG = Tag.Transient("player-mode");

    protected final Logger log;
    protected final UUID id = UUID.randomUUID();

    private final GameServer gameServer;
    private List<GamePlayer> players = new ArrayList<>();

    protected CommandManager commandManager;
    protected InstanceManager instanceManager;


    public Mode(GameServer gameServer) {
        this.log = LoggerFactory.getLogger(getClass());
        this.gameServer = gameServer;
        this.commandManager = MinecraftServer.getCommandManager();
        this.instanceManager = MinecraftServer.getInstanceManager();
    }

    public GameServer getGameServer() {
        return gameServer;
    }

    public UUID getId() {
        return id;
    }

    @ApiStatus.Internal
    public final void enter(GamePlayer player) {
        if (player.getTag(PLAYER_MODE_TAG) != null) {
            var oldMode = player.getTag(PLAYER_MODE_TAG);
            if (oldMode != this) {
                leave(player);
            } else {
                return;
            }
        }
        player.setMode(this);
        players.add(player);
        player.setTag(PLAYER_MODE_TAG, this);

        // Refresh Commands ( used for Mode specific Commands )
        player.refreshCommands();

        // Reset Player
        player.getInventory().clear();
        player.getAttributes().forEach(AttributeInstance::clearModifiers);
        player.setHelmet(ItemStack.AIR);
        player.setChestplate(ItemStack.AIR);
        player.setLeggings(ItemStack.AIR);
        player.setBoots(ItemStack.AIR);
        player.setFood(20);
        player.heal();
        player.setAdditionalHearts(0);
        player.clearEffects();
        player.setGlowing(false);
        player.setFlying(false);
        player.setAllowFlying(false);
        player.setExp(0);
        player.setLevel(0);
        player.clearTitle();

        // Calles the onEnter Method on the Mode
        onEnter(player);
    }

    public abstract void onEnter(GamePlayer player);

    @ApiStatus.Internal
    public final void leave(GamePlayer player) {
        players.remove(player);
        player.setTag(PLAYER_MODE_TAG, null);
        onLeave(player);
    }

    public abstract void onLeave(GamePlayer player);

}

package net.uebliche.server.abyss;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerLoadedEvent;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.uebliche.server.GamePlayer;
import net.uebliche.server.GameServer;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class FakeLoadingScreen {

    private final List<String> steps = List.of(
            "Connecting to Database...",
            "Searching for User...",
            "User data loaded...",
            "Loading World...",
            "Done!"
    );

    private final Player player;
    private final MiniMessage mm = MiniMessage.miniMessage();
    private int step = 0;
    private boolean isLoaded = false;
    private boolean waitingForLoad = false;
    private Task task;
    private int animationTick = 0;
    private CompletableFuture<Boolean> future;

    public FakeLoadingScreen(Player player, CompletableFuture<Boolean> future) {
        this.player = player;
        player.eventNode().addListener(EventListener
                .builder(PlayerLoadedEvent.class)
                .expireCount(1)
                .handler(playerLoadedEvent -> {
                    isLoaded = true;
                })
                .build()
        );
        this.future = future;
        start();
    }

    public static CompletableFuture<Boolean> load(Player player) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        var loading = new FakeLoadingScreen(player, future);
        return future;
    }

    private void start() {
        task = MinecraftServer.getSchedulerManager().buildTask(() -> {
            String subtitle = buildMovingBar();
            if (step < steps.size()) {
                sendTitle(steps.get(step), subtitle);
                if (animationTick % 20 == 0) step++;
            } else if (!waitingForLoad) {
                waitingForLoad = true;
                sendTitle("Waiting for server response...", subtitle);
            } else if (isLoaded && ThreadLocalRandom.current().nextInt(5) == 0) {
                sendTitle("<green><bold>✔ Connected!", "<gray>Teleporting...");
                onFinish();
            }
            player.sendTitlePart(TitlePart.TIMES, Title.Times.times(
                    Duration.ZERO,
                    Duration.ofSeconds(2),
                    Duration.ZERO
            ));
            animationTick++;
        }).repeat(TaskSchedule.tick(2)).schedule(); // schnellere Animation
    }

    private void sendTitle(String title, String subtitle) {
        Component titleComponent = mm.deserialize("<aqua>" + title);
        Component subtitleComponent = mm.deserialize(subtitle);
        player.showTitle(Title.title(titleComponent, subtitleComponent));
    }

    private String buildMovingBar() {
        int length = 20;
        int pos = animationTick % length;

        StringBuilder bar = new StringBuilder("<dark_gray>[");
        for (int i = 0; i < length; i++) {
            if (i == pos) {
                bar.append("<green>█");
            } else {
                bar.append("<gray>█");
            }
        }
        bar.append("<dark_gray>]");
        return bar.toString();
    }

    public void setLoaded(boolean loaded) {
        this.isLoaded = loaded;
    }

    public void onFinish() {
        task.cancel();
        if (player instanceof GamePlayer gamePlayer) {
            future.complete(true);
        } else {
            player.sendMessage(mm.deserialize("<red>Error: Player is not a GamePlayer."));
            future.complete(false);
        }
    }
}

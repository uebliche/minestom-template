package net.uebliche.server.mongodb.objects.user;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.minestom.server.network.player.GameProfile;
import net.uebliche.server.mongodb.objects.Repository;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class UserRepository extends Repository<User> {

    public UserRepository(MongoCollection<User> collection) {
        super(collection);
    }

    protected User injectRepository(User user) {
        user.repository = this;
        return user;
    }

    public CompletableFuture<Stream<User>> findAll() {
        return CompletableFuture.supplyAsync(() -> collection.find().into(new ArrayList<>()).stream().map(this::injectRepository));
    }

    public CompletableFuture<Optional<User>> findByUUID(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(collection.find(idFilter(uuid)).first()).map(this::injectRepository));
    }

    public CompletableFuture<User> findOrCreate(@NotNull GameProfile gameProfile) {
        return findByUUID(gameProfile.uuid()).thenApply(optUser -> optUser.orElseGet(() -> {
            var newUser = new User(gameProfile.uuid(), gameProfile.name());
            collection.insertOne(newUser);
            return injectRepository(newUser);
        }));
    }

    protected CompletableFuture<Boolean> update(UUID uuid, Bson updates) {
        return CompletableFuture.supplyAsync(() -> collection.updateOne(idFilter(uuid), updates).wasAcknowledged());
    }

    public CompletableFuture<Boolean> incrementLoginCount(UUID uuid) {
        return update(uuid, Updates.combine(Updates.inc("loginCount", 1), Updates.set("lastLogin", Instant.now())));
    }
}

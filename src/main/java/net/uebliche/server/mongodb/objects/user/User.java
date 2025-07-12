package net.uebliche.server.mongodb.objects.user;

import net.uebliche.server.mongodb.objects.Element;
import org.bson.codecs.pojo.annotations.BsonId;

import java.time.Instant;
import java.util.UUID;

public class User extends Element<User> {

    @BsonId
    private UUID uuid;
    private String name;
    private Instant created = Instant.now();
    private Instant lastLogin = Instant.now();
    private int loginCount = 0;

    public User() {
    }

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Instant lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }
}

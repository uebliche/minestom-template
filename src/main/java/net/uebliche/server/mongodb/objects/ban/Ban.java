package net.uebliche.server.mongodb.objects.ban;

import net.uebliche.server.mongodb.objects.Element;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import java.util.UUID;

public class Ban extends Element<Ban> {

    @BsonId
    private ObjectId id;

    private UUID userId;
    private String reason;
    private UUID bannedBy;

    public Ban() {
    }

    public Ban(UUID userId, String reason, UUID bannedBy) {
        this.userId = userId;
        this.reason = reason;
        this.bannedBy = bannedBy;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UUID getBannedBy() {
        return bannedBy;
    }

    public void setBannedBy(UUID bannedBy) {
        this.bannedBy = bannedBy;
    }
}

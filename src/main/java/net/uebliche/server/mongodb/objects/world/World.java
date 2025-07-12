package net.uebliche.server.mongodb.objects.world;

import net.uebliche.server.mongodb.objects.Element;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.Binary;
import org.bson.types.ObjectId;

import java.time.Instant;

public class World extends Element<World> {

    @BsonId
    private ObjectId id;
    private String name;
    private Binary data;
    private Instant created = Instant.now();
    private Instant lastModified = Instant.now();
    private Long size = 0L;
    private Long chunkCount = 0L;

    public World(String name, Binary data) {
        this.name = name;
        this.data = data;
    }

    public World() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Binary getData() {
        return data;
    }

    public void setData(Binary data) {
        this.data = data;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastModified() {
        return lastModified;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getChunkCount() {
        return chunkCount;
    }

    public void setChunkCount(Long chunkCount) {
        this.chunkCount = chunkCount;
    }
}

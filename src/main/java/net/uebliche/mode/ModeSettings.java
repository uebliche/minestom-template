package net.uebliche.mode;

import net.uebliche.server.mongodb.objects.Element;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

@BsonDiscriminator(key = "mode")
public class ModeSettings extends Element<ModeSettings> {

    @BsonId
    public ObjectId id;
    public WorldProvider worldProvider;


    @Override
    public String toString() {
        return "ModeSettings{" +
                "id=" + id +
                ", worldProvider=" + worldProvider +
                '}';
    }
}

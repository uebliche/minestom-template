package net.uebliche.server.mongodb.objects.world;

import com.mongodb.client.MongoCollection;
import net.uebliche.server.mongodb.objects.Repository;

public class WorldRepository extends Repository<World> {


    public WorldRepository(MongoCollection<World> collection) {
        super(collection);
    }

}

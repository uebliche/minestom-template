package net.uebliche.server.mongodb.objects.ban;

import com.mongodb.client.MongoCollection;
import net.uebliche.server.mongodb.objects.Repository;

public class BanRepository extends Repository<Ban> {


    public BanRepository(MongoCollection<Ban> collection) {
        super(collection);
    }



}

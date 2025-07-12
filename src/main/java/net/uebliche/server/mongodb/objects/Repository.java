package net.uebliche.server.mongodb.objects;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

public abstract class Repository<T extends Element> {

    protected final MongoCollection<T> collection;

    public Repository(MongoCollection<T> collection){
        this.collection = collection;
    }

    protected T injectRepository(T object){
        object.repository = this;
        return object;
    }

    protected <ID> Bson idFilter(ID id){
        return Filters.eq("_id", id);
    }

}

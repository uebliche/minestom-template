package net.uebliche.server.mongodb.objects;

import org.bson.codecs.pojo.annotations.BsonIgnore;

public abstract class Element<T extends Element<?>> {

    @BsonIgnore
    public Repository<T> repository;

}

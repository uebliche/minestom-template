package net.uebliche.game;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "class", value = "persistent")
public class PersistentGameSettings extends GameSettings {

    public int mapChangeEveryXMinutes = 10;

}

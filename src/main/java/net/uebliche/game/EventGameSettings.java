package net.uebliche.game;

import net.uebliche.server.GameServer;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "class", value = "event")
public abstract class EventGameSettings extends GameSettings {


}

package net.uebliche.game;

import net.uebliche.server.GameServer;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "class", value = "timedgame")
public abstract class TimedGameSettings extends GameSettings {


}

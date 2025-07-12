package net.uebliche.game;

import net.uebliche.mode.ModeSettings;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "class", value = "game")
public abstract class GameSettings extends ModeSettings {

    public int maxPlayers = 100;


}

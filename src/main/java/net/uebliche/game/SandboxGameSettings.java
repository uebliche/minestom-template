package net.uebliche.game;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "class", value = "sandboxgame")
public class SandboxGameSettings extends GameSettings{
}

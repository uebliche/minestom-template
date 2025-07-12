package net.uebliche.demo.game.ffa;

import net.uebliche.game.PresistentGameSettings;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "class", value = "classicffa")
public class ClassicFFASettings extends PresistentGameSettings {

    public int testCLassicInt = 12;
    public String testClassicString = "test";

    @Override
    public String toString() {
        return "ClassicFFASettings{" +
                "testCLassicInt=" + testCLassicInt +
                ", testClassicString='" + testClassicString + '\'' +
                ", mapChangeEveryXMinutes=" + mapChangeEveryXMinutes +
                ", maxPlayers=" + maxPlayers +
                ", id=" + id +
                ", repository=" + repository +
                '}';
    }
}

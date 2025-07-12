package net.uebliche.server.mongodb.objects.settings;

import com.mongodb.client.MongoCollection;
import net.uebliche.game.ffa.ClassicFFASettings;
import net.uebliche.mode.ModeSettings;
import net.uebliche.server.mongodb.objects.Repository;
import org.bson.types.ObjectId;

public class SettingsRepository extends Repository<ModeSettings> {


    public SettingsRepository(MongoCollection<ModeSettings> collection) {
        super(collection);
    }

    public void insert(ModeSettings settings) {
        collection.insertOne(settings);
    }

    public <Settings extends ModeSettings> Settings find(ObjectId id, Class<Settings> clazz) {
        return collection.find(idFilter(id), clazz).first();
    }
}

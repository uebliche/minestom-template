package net.uebliche.mode;

import org.bson.types.ObjectId;

public record WorldProvider(
        String type,
        String anvilPath,
        ObjectId polarId
) {

    static WorldProvider ofAnvil(String anvilPath) {
        return new WorldProvider("anvil", anvilPath, null);
    }

    static WorldProvider ofPolar(ObjectId polarId) {
        return new WorldProvider("polar", null, polarId);
    }

    public boolean isAnvil() {
        return "anvil".equals(type);
    }

    public boolean isPolar() {
        return "polar".equals(type);
    }

}

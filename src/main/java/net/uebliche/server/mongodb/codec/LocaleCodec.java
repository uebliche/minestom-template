package net.uebliche.server.mongodb.codec;

import org.bson.BsonInvalidOperationException;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.Locale;

public class LocaleCodec implements Codec<Locale> {
    
    @Override
    public void encode(BsonWriter writer, Locale value, EncoderContext encoderContext) {
        if (value == null) {
            writer.writeNull();
        } else {
            writer.writeString(value.toLanguageTag());
        }
    }
    
    @Override
    public Locale decode(BsonReader reader, DecoderContext decoderContext) {
        BsonType type = reader.getCurrentBsonType();
        if (type == BsonType.NULL) {
            reader.readNull();
            return null;
        } else if (type == BsonType.STRING) {
            String tag = reader.readString();
            return Locale.forLanguageTag(tag);
        } else {
            throw new BsonInvalidOperationException("Cannot decode Locale from BsonType: " + type);
        }
    }
    
    @Override
    public Class<Locale> getEncoderClass() {
        return Locale.class;
    }
}

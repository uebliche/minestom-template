package net.uebliche.server.mongodb.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.BsonType;
import org.bson.BsonInvalidOperationException;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.time.Instant;

public class InstantCodec implements Codec<Instant> {
    
    @Override
    public Instant decode(BsonReader reader, DecoderContext decoderContext) {
        BsonType type = reader.getCurrentBsonType();
        if (type == BsonType.NULL) {
            reader.readNull();
            return null;
        } else if (type == BsonType.DATE_TIME) {
            return Instant.ofEpochMilli(reader.readDateTime());
        } else {
            throw new BsonInvalidOperationException("Cannot decode Instant from BsonType: " + type);
        }
    }
    
    @Override
    public void encode(BsonWriter writer, Instant value, EncoderContext encoderContext) {
        if (value == null) {
            writer.writeNull();
        } else {
            writer.writeDateTime(value.toEpochMilli());
        }
    }
    
    @Override
    public Class<Instant> getEncoderClass() {
        return Instant.class;
    }
}

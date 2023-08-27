package com.example.atiperaapi.serialization;

import com.example.atiperaapi.model.Owner;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class OwnerSerializer extends JsonSerializer<Owner> {
    @Override
    public void serialize(Owner owner, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(owner.getLogin());
    }
}

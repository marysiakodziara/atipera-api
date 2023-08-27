package com.example.atiperaapi.serialization;

import com.example.atiperaapi.model.Commit;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class CommitSerializer extends JsonSerializer<Commit> {
    @Override
    public void serialize(Commit commit, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(commit.getSha());
    }
}

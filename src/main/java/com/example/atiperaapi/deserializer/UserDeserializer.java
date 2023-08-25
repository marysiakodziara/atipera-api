package com.example.atiperaapi.deserializer;

import com.example.atiperaapi.model.GitHubRepo;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import java.lang.reflect.Type;

public class UserDeserializer implements JsonDeserializer<String> {
    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return json.getAsJsonObject().get("login").getAsString();
    }
}

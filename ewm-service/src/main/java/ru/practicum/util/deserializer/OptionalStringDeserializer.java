package ru.practicum.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Optional;

public class OptionalStringDeserializer extends JsonDeserializer<Optional<String>> {

    @Override
    public Optional<String> deserialize(JsonParser p, DeserializationContext txt) throws IOException {
        if (p.getCurrentToken() == JsonToken.VALUE_NULL || p.getText() == null) {
            return Optional.empty();
        }
        String str = p.getText();
        return Optional.of(str);
    }
}
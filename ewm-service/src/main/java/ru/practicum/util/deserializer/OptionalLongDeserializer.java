package ru.practicum.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Optional;

public class OptionalLongDeserializer extends JsonDeserializer<Optional<Long>> {

    @Override
    public Optional<Long> deserialize(JsonParser p, DeserializationContext txt) throws IOException {
        JsonToken currentToken = p.getCurrentToken();
        if (currentToken == JsonToken.VALUE_NULL) {
            return Optional.empty();
        } else if (currentToken == JsonToken.VALUE_STRING && p.getText().isEmpty()) {
            return Optional.empty();
        } else {
            Long id = p.getValueAsLong();
            return Optional.ofNullable(id);
        }
    }
}
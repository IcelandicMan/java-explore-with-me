package ru.practicum.compilation.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.util.annotation.OptionalStringSize;
import ru.practicum.util.deserializer.OptionalStringDeserializer;

import java.util.Optional;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationRequestUpdateDto {
    private Set<Long> events;

    private Boolean pinned;

    @JsonDeserialize(using = OptionalStringDeserializer.class)
    @OptionalStringSize(min = 1, max = 50)
    private Optional<String> title = Optional.empty();
}


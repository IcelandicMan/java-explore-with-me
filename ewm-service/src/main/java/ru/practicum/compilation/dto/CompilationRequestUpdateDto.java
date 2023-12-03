package ru.practicum.compilation.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.util.annotation.OptionalStringSize;

import java.util.Optional;
import java.util.Set;

@Data
@Builder
public class CompilationRequestUpdateDto {
    private Set<Long> events;

    private Boolean pinned;

    @OptionalStringSize(min = 1, max = 50)
    private Optional<String> title;
}


package ru.practicum.mapper.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.event.EventResponseShortDto;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationResponseDto {

    private Long id;

    private Boolean pinned;

    private String title;

    private Set<EventResponseShortDto> events;
}


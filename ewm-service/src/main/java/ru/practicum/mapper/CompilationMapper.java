package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.event.EventResponseShortDto;
import ru.practicum.mapper.compilation.CompilationRequestNewDto;
import ru.practicum.mapper.compilation.CompilationResponseDto;
import ru.practicum.model.Compilation;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public Compilation compilationResponseNewDtoToCompilation(CompilationRequestNewDto compilationDto) {
        return Compilation.builder()
                .title(compilationDto.getTitle())
                .pinned(compilationDto.getPinned())
                .build();
    }

    public CompilationResponseDto compilationToCompilationResponseDto(Compilation compilation) {
        Set<EventResponseShortDto> eventShortDtoSet = compilation.getEvents().stream()
                .map(EventMapper::eventToEventResponseShortDto).collect(Collectors.toSet());

        return CompilationResponseDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(eventShortDtoSet)
                .build();
    }
}

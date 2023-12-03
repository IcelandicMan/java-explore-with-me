package ru.practicum.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.compilation.dto.CompilationRequestNewDto;
import ru.practicum.compilation.dto.CompilationResponseDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventResponseShortDto;
import ru.practicum.event.mapper.EventMapper;

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

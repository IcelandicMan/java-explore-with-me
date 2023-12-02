package ru.practicum.service.compilation;

import ru.practicum.mapper.compilation.CompilationRequestNewDto;
import ru.practicum.mapper.compilation.CompilationRequestUpdateDto;
import ru.practicum.mapper.compilation.CompilationResponseDto;

import java.util.List;

public interface CompilationService {

    CompilationResponseDto createCompilation(CompilationRequestNewDto compilationNewDto);

    void deleteCompilation(Long compId);

    CompilationResponseDto updateCompilation(Long compId, CompilationRequestUpdateDto compilationUpdateDto);

    List<CompilationResponseDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationResponseDto getCompilationById(Long compId);
}


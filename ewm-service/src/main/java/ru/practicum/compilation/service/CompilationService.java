package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationRequestNewDto;
import ru.practicum.compilation.dto.CompilationRequestUpdateDto;
import ru.practicum.compilation.dto.CompilationResponseDto;

import java.util.List;

public interface CompilationService {

    CompilationResponseDto createCompilation(CompilationRequestNewDto compilationNewDto);

    void deleteCompilation(Long compId);

    CompilationResponseDto updateCompilation(Long compId, CompilationRequestUpdateDto compilationUpdateDto);

    List<CompilationResponseDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationResponseDto getCompilationById(Long compId);
}


package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationRequestNewDto;
import ru.practicum.compilation.dto.CompilationRequestUpdateDto;
import ru.practicum.compilation.dto.CompilationResponseDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class CompilationAdminController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CompilationResponseDto createCompilation(@Valid @RequestBody CompilationRequestNewDto compilationRequestUpdateDto) {
        log.info("Запрошено создание подборки событий: {} ", compilationRequestUpdateDto);
        CompilationResponseDto compilationResponseDto = compilationService.createCompilation(compilationRequestUpdateDto);
        log.info("Запрос на создание подборки событий выполнен: {} ", compilationRequestUpdateDto);
        return compilationResponseDto;
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Запрошено подборки событий с id {} ", compId);
        compilationService.deleteCompilation(compId);
        log.info("Запрос на удаление подборки событий с id {} выполнен", compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CompilationResponseDto updateCompilationByAdmin(@Valid @RequestBody CompilationRequestUpdateDto compilationRequestUpdateDto,
                                                           @PathVariable Long compId) {
        log.info("Запрос от Админа на обновление подборки событий с id {}: {} ", compId, compilationRequestUpdateDto);
        CompilationResponseDto compilation = compilationService.updateCompilation(compId, compilationRequestUpdateDto);
        log.info("Запрос от Админа на обновление  подборки событий с id {} выполнен: {} ", compId, compilation);
        return compilation;
    }
}

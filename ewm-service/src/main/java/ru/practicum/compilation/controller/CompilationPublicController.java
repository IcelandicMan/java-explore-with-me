package ru.practicum.compilation.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationResponseDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationPublicController {

    private final CompilationService compilationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationResponseDto> getCompilations(@RequestParam(defaultValue = "false", name = "pinned") Boolean pinned,
                                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                        @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("Запрос на получение всех подборок событий с параметрами pinned = {}, from = {}, size = {}",
                pinned, from, size);
        List<CompilationResponseDto> compilationResponseDtoList = compilationService.getCompilations(pinned, from, size);
        log.info("Запрос на получение всех подборок событий с параметрами pinned = {}, from = {}, size = {} Выполнен: {}",
                pinned, from, size, compilationResponseDtoList);
        return compilationResponseDtoList;
    }

    @GetMapping("/{compId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CompilationResponseDto getCompilationById(@PathVariable Long compId) {
        log.info("Запрос на предоставление подборок событий под id {}", compId);
        CompilationResponseDto compilationResponseDto = compilationService.getCompilationById(compId);
        log.info("Запрос на предоставление подборок событий под id {} Выполнен: {} ", compId, compilationResponseDto);
        return compilationResponseDto;
    }
}

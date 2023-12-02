package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryResponseDto;
import ru.practicum.dto.event.EventResponseFullDto;
import ru.practicum.dto.event.EventResponseShortDto;
import ru.practicum.mapper.compilation.CompilationResponseDto;
import ru.practicum.service.category.CategoryService;
import ru.practicum.service.compilation.CompilationService;
import ru.practicum.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PublicController {

    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;

    @GetMapping("/categories")
    @ResponseStatus(value = HttpStatus.OK)
    public List<CategoryResponseDto> getCategories(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Запрошен список категорий c параметрами from {}, size {}", from, size);
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CategoryResponseDto getCategory(@PathVariable Long catId) {
        log.info("Запрошена категория c id {}", catId);
        return categoryService.getCategory(catId);
    }

    @GetMapping("/events/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public EventResponseFullDto getEventByCreator(@PathVariable Long id, HttpServletRequest request) {
        log.info("Запрошено событие с id {} от пользователя public ", id);
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        EventResponseFullDto event = eventService.getEventByPublic(id, uri, ip);
        log.info("Запрос на предоставление данные по событию с id {} выполнен: {} \n Статика по {} и ip {} записаны",
                id, event, uri, ip);
        return event;
    }

    @GetMapping("/events")
    @ResponseStatus(value = HttpStatus.OK)
    public List<EventResponseShortDto> getEventsByPublic(@RequestParam(required = false, name = "text") String text,
                                                         @RequestParam(required = false, name = "categories") List<Long> categories,
                                                         @RequestParam(required = false, name = "paid") Boolean paid,
                                                         @RequestParam(required = false, name = "rangeStart")
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                         @RequestParam(required = false, name = "rangeEnd")
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                         @RequestParam(required = false, defaultValue = "false", name = "onlyAvailable") Boolean onlyAvailable,
                                                         @RequestParam(required = false, name = "sort") String sort,
                                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                         HttpServletRequest request) {

        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();

        log.info("Получение событий Public по следующим параметрам: text = {}, categories = {}, paid = {}, " +
                        "rangeStart = {}, rangeEnd = {}, onlyAvailable = {}, sort = {}, from = {}, size = {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        List<EventResponseShortDto> eventsList = eventService.getEventsByPublic(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size, uri, ip);

        log.info("Информация предоставлена");
        return eventsList;
    }


    @GetMapping("/compilations")
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

    @GetMapping("/compilations/{compId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CompilationResponseDto getCompilationById(@PathVariable Long compId) {
        log.info("Запрос на предоставление подборок событий под id {}", compId);
        CompilationResponseDto compilationResponseDto = compilationService.getCompilationById(compId);
        log.info("Запрос на предоставление подборок событий под id {} Выполнен: {} ", compId, compilationResponseDto);
        return compilationResponseDto;
    }


}

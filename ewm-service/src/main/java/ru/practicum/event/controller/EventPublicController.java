package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.service.CommentService;
import ru.practicum.event.dto.EventResponseFullDto;
import ru.practicum.event.dto.EventResponseShortDto;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventPublicController {

    private final EventService eventService;
    private final CommentService commentService;

    @GetMapping("/{id}")
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

    @GetMapping
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
}

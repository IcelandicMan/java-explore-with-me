package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventRequestDto;
import ru.practicum.event.dto.EventRequestUpdateDto;
import ru.practicum.event.dto.EventResponseFullDto;
import ru.practicum.event.dto.EventResponseShortDto;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestUpdateDtoRequest;
import ru.practicum.request.dto.RequestUpdateDtoResult;
import ru.practicum.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class EventPrivateController {

    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventResponseFullDto createEvent(@Valid @RequestBody EventRequestDto eventRequestDto,
                                            @PathVariable Long userId) {

        log.info("Запрошено от пользователя с id {} создание события: {}", userId, eventRequestDto);
        EventResponseFullDto event = eventService.createEvent(userId, eventRequestDto);
        log.info("Запрос на создание от пользователя с id {} выполнен, событие создано: {} ", userId, event);
        return event;
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<EventResponseShortDto> getAllEventsByUserId(@PathVariable Long userId,
                                                            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("Запрошен список всех событий, добавленных пользователем с id {} c параметрами from {}, size {}",
                userId, from, size);
        return eventService.findByInitiatorId(userId, from, size);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(value = HttpStatus.OK)
    public EventResponseFullDto getEventByCreator(@PathVariable Long userId,
                                                  @PathVariable Long eventId) {
        log.info("Запрошено событие с id {} от пользователя с id {}", eventId, userId);
        EventResponseFullDto event = eventService.getEventByCreator(userId, eventId);
        log.info("Запрос на предоставление данные по событию с id {} выполнен: ", event);
        return event;
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(value = HttpStatus.OK)
    public EventResponseFullDto updateEventByOwner(@PathVariable Long userId,
                                                   @PathVariable Long eventId,
                                                   @Valid @RequestBody EventRequestUpdateDto eventRequestDto) {
        log.info("Запрошено обновление события под id: {}, от пользователя под id: {}. Событие: {}",
                eventId, userId, eventRequestDto);
        EventResponseFullDto updatedEvent = eventService.updateEventByCreator(userId, eventId, eventRequestDto);
        log.info("Запрос выполнен, событие обновлено: {} ", updatedEvent);
        return updatedEvent;
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(value = HttpStatus.OK)
    private List<RequestDto> getRequestsForEventIdByUserId(@PathVariable Long userId,
                                                           @PathVariable Long eventId) {
        log.info("Получение всех запросов на участие в событии события с id {}", eventId);
        List<RequestDto> requestDtoList = requestService.getRequestsForEventIdByUserId(userId, eventId);
        log.info("Коллекция всех запросов на участие в событии события с id: {} \n " +
                "получена: {} ", eventId, requestDtoList);
        return requestDtoList;
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(value = HttpStatus.OK)
    private RequestUpdateDtoResult updateStatusRequestsForEventIdByUserId(@PathVariable Long userId,
                                                                          @PathVariable Long eventId,
                                                                          @RequestBody RequestUpdateDtoRequest requestDto) {
        log.info("Запрос на изменения статуса заявки для события под id: {}, пользователем под id {} \n" +
                "Список запросов: {} ", eventId, userId, requestDto);
        RequestUpdateDtoResult requestUpdateDtoResult = requestService.updateStatusRequestsForEventIdByUserId(requestDto, userId, eventId);
        log.info("Запрос на изменения статуса заявки для события под id: {}, пользователем под id {} \n " +
                "Выполнен: {} ", eventId, userId, requestUpdateDtoResult);
        return requestUpdateDtoResult;
    }
}

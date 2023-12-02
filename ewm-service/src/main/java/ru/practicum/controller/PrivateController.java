package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventRequestDto;
import ru.practicum.dto.event.EventRequestUpdateDto;
import ru.practicum.dto.event.EventResponseFullDto;
import ru.practicum.dto.event.EventResponseShortDto;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestUpdateDtoRequest;
import ru.practicum.dto.request.RequestUpdateDtoResult;
import ru.practicum.service.event.EventService;
import ru.practicum.service.request.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}")
public class PrivateController {

    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping("/events") // 1
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventResponseFullDto createEvent(@Valid @RequestBody EventRequestDto eventRequestDto,
                                            @PathVariable Long userId) {

        log.info("Запрошено от пользователя с id {} создание события: {}", userId, eventRequestDto);
        EventResponseFullDto event = eventService.createEvent(userId, eventRequestDto);
        log.info("Запрос на создание от пользователя с id {} выполнен, событие создано: {} ", userId, event);
        return event;
    }

    @GetMapping("/events") // 2
    @ResponseStatus(value = HttpStatus.OK)
    public List<EventResponseShortDto> getAllEventsByUserId(@PathVariable Long userId,
                                                            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("Запрошен список всех событий, добавленных пользователем с id {} c параметрами from {}, size {}",
                userId, from, size);
        return eventService.findByInitiatorId(userId, from, size);
    }

    @GetMapping("/events/{eventId}") // 3
    @ResponseStatus(value = HttpStatus.OK)
    public EventResponseFullDto getEventByCreator(@PathVariable Long userId,
                                                  @PathVariable Long eventId) {
        log.info("Запрошено событие с id {} от пользователя с id {}", eventId, userId);
        EventResponseFullDto event = eventService.getEventByCreator(userId, eventId);
        log.info("Запрос на предоставление данные по событию с id {} выполнен: ", event);
        return event;
    }

    @PatchMapping("/events/{eventId}") // 4
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

    @PostMapping("/requests")
    @ResponseStatus(value = HttpStatus.CREATED) // 5
    public RequestDto createRequest(@PathVariable Long userId,
                                    @RequestParam Long eventId) {
        log.info("Запрос от пользователя с id: {} на участие в Событии под id: {}.", userId, eventId);
        RequestDto requestDto = requestService.createRequest(userId, eventId);
        log.info("Запрос от пользователя с id: {} на участие в Событии под id: {} создан: {}.", userId, eventId, requestDto);
        return requestDto;
    }

    @GetMapping("/requests")
    @ResponseStatus(value = HttpStatus.OK) // 6
    public List<RequestDto> getRequestsByUserId(@PathVariable Long userId) {
        log.info("Запрос от пользователя с id: {} на предоставление всех запросов на участие в событиях.", userId);
        List<RequestDto> requestList = requestService.getRequestsByUserId(userId);
        log.info("Запрос от пользователя с id: {} на предоставление всех запросов на участие в событиях выполнен {}: ",
                userId, requestList);
        return requestList;
    }

    @PatchMapping("/requests/{requestId}/cancel") // 7
    @ResponseStatus(value = HttpStatus.OK)
    public RequestDto cancelRequest(@PathVariable Long userId,
                                    @PathVariable Long requestId) {
        log.info("Запрос от пользователя с id: {} на отмену запроса на участие в события под id: {}", userId, requestId);
        RequestDto requestDto = requestService.cancelRequest(userId, requestId);
        log.info("Запрос на отмену в участии в событии под id: {}, от пользователя с id:{},  Выполнен {}: ",
                requestId, userId, requestDto);
        return requestDto;
    }

    @GetMapping("/events/{eventId}/requests") // 8
    @ResponseStatus(value = HttpStatus.OK)
    private List<RequestDto> getRequestsForEventIdByUserId(@PathVariable Long userId,
                                                           @PathVariable Long eventId) {
        log.info("Получение всех запросов на участие в событии события с id {}", eventId);
        List<RequestDto> requestDtoList = requestService.getRequestsForEventIdByUserId(userId, eventId);
        log.info("Коллекция всех запросов на участие в событии события с id: {} \n " +
                "получена: {} ", eventId, requestDtoList);
        return requestDtoList;
    }

    @PatchMapping("/events/{eventId}/requests") // 9
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


package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class RequestPrivateController {

    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public RequestDto createRequest(@PathVariable Long userId,
                                    @RequestParam Long eventId) {
        log.info("Запрос от пользователя с id: {} на участие в Событии под id: {}.", userId, eventId);
        RequestDto requestDto = requestService.createRequest(userId, eventId);
        log.info("Запрос от пользователя с id: {} на участие в Событии под id: {} создан: {}.", userId, eventId, requestDto);
        return requestDto;
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<RequestDto> getRequestsByUserId(@PathVariable Long userId) {
        log.info("Запрос от пользователя с id: {} на предоставление всех запросов на участие в событиях.", userId);
        List<RequestDto> requestList = requestService.getRequestsByUserId(userId);
        log.info("Запрос от пользователя с id: {} на предоставление всех запросов на участие в событиях выполнен {}: ",
                userId, requestList);
        return requestList;
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(value = HttpStatus.OK)
    public RequestDto cancelRequest(@PathVariable Long userId,
                                    @PathVariable Long requestId) {
        log.info("Запрос от пользователя с id: {} на отмену запроса на участие в события под id: {}", userId, requestId);
        RequestDto requestDto = requestService.cancelRequest(userId, requestId);
        log.info("Запрос на отмену в участии в событии под id: {}, от пользователя с id:{},  Выполнен {}: ",
                requestId, userId, requestDto);
        return requestDto;
    }
}

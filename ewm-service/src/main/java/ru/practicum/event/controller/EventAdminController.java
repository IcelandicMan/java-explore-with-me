package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentResponseParentDto;
import ru.practicum.comment.dto.CommentUpdateAdminDto;
import ru.practicum.comment.service.CommentService;
import ru.practicum.event.dto.EventRequestUpdateDto;
import ru.practicum.event.dto.EventResponseFullDto;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventAdminController {

    private final EventService eventService;
    private final CommentService commentService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<EventResponseFullDto> getEventsByAdmin(@RequestParam(required = false, name = "users") List<Long> users,
                                                       @RequestParam(required = false, name = "states") List<String> states,
                                                       @RequestParam(required = false, name = "categories") List<Long> categories,
                                                       @RequestParam(required = false, name = "rangeStart")
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                       @RequestParam(required = false, name = "rangeEnd")
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                       @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                       @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("Получение событий от Админа по следующим параметрам: users = {}, states = {}, categories = {}, " +
                        "rangeStart = {}, rangeEnd = {}, from = {}, size = {}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        List<EventResponseFullDto> events = eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
        log.info("Информация предоставлена: {} ", events);
        return events;
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(value = HttpStatus.OK)
    public EventResponseFullDto updateEventByAdmin(@Valid @RequestBody EventRequestUpdateDto eventUpdateDto,
                                                   @PathVariable Long eventId) {
        log.info("Запрос от Админа на обновление события с id {}: {} ", eventId, eventUpdateDto);
        EventResponseFullDto event = eventService.updateEventByAdmin(eventUpdateDto, eventId);
        log.info("Запрос от Админа на обновление события с id {} выполнен, событие обновлено : {} ", eventId, event);
        return event;
    }

    @PatchMapping("/comments")
    @ResponseStatus(value = HttpStatus.OK)
    public List<CommentResponseParentDto> updateCommentsStatusByAdmin(@RequestBody CommentUpdateAdminDto commentUpdateAdminDto) {
        log.info("Запрос от Admin  обновление статусов комментариев: {} ", commentUpdateAdminDto);
        List<CommentResponseParentDto> comments = commentService.updateCommentByAdmin(commentUpdateAdminDto);
        log.info("\"Запрос от Admin  обновление статусов комментариев выполнен,\n" +
                "комментарии обновлены: {} ", comments);
        return comments;
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(@PathVariable("commentId") Long commentId) {
        log.info("Запрошено удаление комментария с id {} ", commentId);
        commentService.deleteCommentByAdmin(commentId);
        log.info("Запрос на удаление комментария id {} выполнен", commentId);
    }
}

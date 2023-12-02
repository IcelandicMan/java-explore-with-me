package ru.practicum.service.event;

import ru.practicum.dto.event.EventRequestDto;
import ru.practicum.dto.event.EventRequestUpdateDto;
import ru.practicum.dto.event.EventResponseFullDto;
import ru.practicum.dto.event.EventResponseShortDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventResponseFullDto createEvent(Long userId, EventRequestDto event);

    List<EventResponseShortDto> findByInitiatorId(Long initiatorId, Integer from, Integer size);

    EventResponseFullDto getEventByCreator(Long userId, Long eventId);

    EventResponseFullDto getEventByPublic(Long eventId, String uri, String ip);

    EventResponseFullDto updateEventByCreator(Long userId, Long eventId, EventRequestUpdateDto event);


    List<EventResponseFullDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventResponseFullDto updateEventByAdmin(EventRequestUpdateDto eventUpdateDto, Long eventId);

    public List<EventResponseShortDto> getEventsByPublic(String text, List<Long> categories, Boolean paid,
                                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                                         String sort, Integer from, Integer size, String uri, String ip);
}

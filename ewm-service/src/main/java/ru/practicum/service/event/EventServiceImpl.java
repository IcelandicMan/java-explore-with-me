package ru.practicum.service.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatisticClient;
import ru.practicum.dto.RequestHitDto;
import ru.practicum.dto.ResponseHitDto;
import ru.practicum.dto.event.EventRequestDto;
import ru.practicum.dto.event.EventRequestUpdateDto;
import ru.practicum.dto.event.EventResponseFullDto;
import ru.practicum.dto.event.EventResponseShortDto;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.exception.EventNotFoundException;
import ru.practicum.exception.EventStateException;
import ru.practicum.exception.ValidationException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.LocationRepository;
import ru.practicum.util.UnionService;
import ru.practicum.util.emums.State;
import ru.practicum.util.emums.StateAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.dto.Constants.DATE_TIME_FORMATTER;
import static ru.practicum.util.Util.START_HISTORY;
import static ru.practicum.util.emums.State.*;
import static ru.practicum.util.emums.StateAction.*;

@Slf4j
@Service
@RequiredArgsConstructor

public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UnionService unionService;
    private final LocationRepository locationRepository;
    private final StatisticClient client;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public EventResponseFullDto createEvent(Long userId, EventRequestDto event) {
        User user = unionService.getUserOrNotFound(userId);
        Category category = unionService.getCategoryOrNotFound(event.getCategory());
        Location location = locationRepository.save(LocationMapper.locationDtoToLocation(event.getLocation()));
        Event createdEvent = EventMapper.eventRequestDtoToEvent(event, user, category, location);
        return EventMapper.eventToEventResponseFullDto(eventRepository.save(createdEvent));
    }

    @Override
    public List<EventResponseShortDto> findByInitiatorId(Long initiatorId, Integer from, Integer size) {
        User user = unionService.getUserOrNotFound(initiatorId);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Event> eventsList;
        eventsList = eventRepository.findByInitiatorId(initiatorId, pageRequest);
        return eventsList.stream()
                .map(EventMapper::eventToEventResponseShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventResponseFullDto getEventByCreator(Long userId, Long eventId) {
        User user = unionService.getUserOrNotFound(userId);
        Event event = unionService.getEventOrNotFound(eventId);
        unionService.userIsEventCreator(user, event);
        return EventMapper.eventToEventResponseFullDto(eventRepository.findByInitiatorIdAndId(userId, eventId));
    }

    @Override
    public EventResponseFullDto getEventByPublic(Long eventId, String uri, String ip) {
        Event event = unionService.getEventOrNotFound(eventId);
        if (!event.getState().equals(PUBLISHED)) {
            throw new EventNotFoundException("Событие должно быть опубликовано");
        }
        sendInfo(uri, ip);
        event.setViews(getViewsEventById(eventId));
        return EventMapper.eventToEventResponseFullDto(eventRepository.save(event));
    }


    @Override
    public EventResponseFullDto updateEventByCreator(Long userId, Long eventId, EventRequestUpdateDto updatedEvent) {
        User user = unionService.getUserOrNotFound(userId);
        Event event = unionService.getEventOrNotFound(eventId);
        unionService.userIsEventCreator(user, event);
        if (event.getState().equals(PUBLISHED)) {
            throw new EventStateException(String.format("Пользователь с id%s не может обновить событие под Id %s, " +
                    "поскольку событие уже опубликовано", userId, eventId));
        }
        updatedEvent.getEventDate().ifPresent(date -> {
            if (date.isAfter(LocalDateTime.now().plusHours(2)) || date.isEqual(LocalDateTime.now().plusHours(2))) {
                event.setEventDate(date);
            } else {
                throw new ValidationException("Дата и время на которые намечено событие не может быть раньше, " +
                        "чем через два часа от текущего момента");
            }
        });

        baseUpdateEvent(event, updatedEvent);
        return EventMapper.eventToEventResponseFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventResponseFullDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                                       LocalDateTime startTime, LocalDateTime endTime,
                                                       Integer from, Integer size) {
        List<State> statesValue = new ArrayList<>();
        if (states != null) {
            for (String state : states) {
                statesValue.add(State.getStateValue(state));
            }
        }

        if (startTime != null && endTime != null) {
            if (startTime.isAfter(endTime)) {
                throw new ValidationException("Начало события должно быть раньше его окончания");
            }
        }

        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Event> eventsList;
        eventsList = eventRepository.findEventsByAdminFromParam(users, statesValue, categories, startTime, endTime, pageRequest);
        return eventsList.stream()
                .map(EventMapper::eventToEventResponseFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventResponseFullDto updateEventByAdmin(EventRequestUpdateDto updatedEvent, Long eventId) {

        Event event = unionService.getEventOrNotFound(eventId);

        if (updatedEvent.getStateAction() != null) {
            if (updatedEvent.getStateAction().equals(PUBLISH_EVENT)) {
                if (!event.getState().equals(PENDING)) {
                    throw new EventStateException(String.format("Событие по id: %s уже опубликовано", event.getTitle()));
                }
                event.setPublishedOn(LocalDateTime.now());

                if (updatedEvent.getEventDate().isPresent()) {
                    if (updatedEvent.getEventDate().get().isAfter(event.getPublishedOn().plusHours(1)) ||
                            updatedEvent.getEventDate().get().isEqual(event.getPublishedOn().plusHours(1))) {
                        event.setEventDate(updatedEvent.getEventDate().get());
                    } else {
                        throw new ValidationException("Дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
                    }
                }
                event.setState(PUBLISHED);
            } else {
                if (!event.getState().equals(PENDING)) {
                    throw new EventStateException(String.format("Событие под id: %s, не может быть обновлено, " +
                            "поскольку имеет статус \"PENDING\"", event.getTitle()));
                }
                event.setState(State.CANCELED);
            }
        }

        Event eventUpdated = baseUpdateEvent(event, updatedEvent);
        return EventMapper.eventToEventResponseFullDto(eventRepository.save(eventUpdated));
    }

    @Override
    public List<EventResponseShortDto> getEventsByPublic(String text, List<Long> categories, Boolean paid,
                                                         LocalDateTime startTime, LocalDateTime endTime,
                                                         Boolean onlyAvailable, String sort, Integer from, Integer size,
                                                         String uri, String ip) {
        if (startTime != null && endTime != null) {
            if (startTime.isAfter(endTime)) {
                throw new ValidationException("Время начала события должно быть до его окончания");
            }
        }
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findEventsByPublicFromParam(text, categories, paid, startTime, endTime,
                onlyAvailable, sort, pageRequest);
        sendInfo(uri, ip);

        for (Event event : events) {
            event.setViews(getViewsEventById(event.getId()));
            eventRepository.save(event);
        }

        return events.stream()
                .map(EventMapper::eventToEventResponseShortDto)
                .collect(Collectors.toList());
    }

    private void sendInfo(String uri, String ip) {
        RequestHitDto hitDto = RequestHitDto.builder()
                .app("ewm-service")
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .build();
        client.createHit(hitDto);
    }

    private Long getViewsEventById(Long eventId) {
        List<String> uris = new ArrayList<>();
        String uri = "/events/" + eventId;
        uris.add(uri);
        ResponseEntity<Object> response = client.getHits(START_HISTORY, LocalDateTime.now(), uris, true);
        List<ResponseHitDto> result = objectMapper.convertValue(response.getBody(), new TypeReference<>() {
        });

        if (result.isEmpty()) {
            return 0L;
        } else {
            return result.get(0).getHits();
        }
    }

    private Event baseUpdateEvent(Event event, EventRequestUpdateDto updatedEvent) {
        final Boolean eventPaid = event.getPaid();
        final String annotation;
        final Long categoryId = updatedEvent.getCategory();
        final String description;
        final LocationDto location = updatedEvent.getLocation();
        final Boolean paid = updatedEvent.getPaid();
        final Long participantLimit = updatedEvent.getParticipantLimit();
        final Boolean requestModeration = updatedEvent.getRequestModeration();
        final StateAction stateAction = updatedEvent.getStateAction();
        final String title;

        if (updatedEvent.getAnnotation().isPresent()) {
            annotation = updatedEvent.getAnnotation().get();
            event.setAnnotation(annotation);
        }
        if (categoryId != null) {
            event.setCategory(unionService.getCategoryOrNotFound(categoryId));
        }
        if (updatedEvent.getDescription().isPresent()) {
            description = updatedEvent.getDescription().get();
            event.setDescription(description);
        }
        if (paid != null) {
            event.setPaid(paid);
        }
        if (location != null) {
            event.setLocation(LocationMapper.locationDtoToLocation(location));
            locationRepository.save(event.getLocation());
        }
        if (participantLimit != null && participantLimit != 0) {
            event.setParticipantLimit(participantLimit);
        }
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }
        if (stateAction != null) {
            if (stateAction == PUBLISH_EVENT) {
                event.setState(PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (stateAction == REJECT_EVENT || stateAction == CANCEL_REVIEW) {
                event.setState(CANCELED);
                event.setPaid(eventPaid);
            } else if (stateAction == SEND_TO_REVIEW) {
                event.setState(PENDING);
                event.setPaid(eventPaid);
            }
        }
        if (updatedEvent.getTitle().isPresent()) {
            title = updatedEvent.getTitle().get();
            event.setTitle(title);
        }
        return event;
    }
}


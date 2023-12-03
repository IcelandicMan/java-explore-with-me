package ru.practicum.request.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestUpdateDtoRequest;
import ru.practicum.request.dto.RequestUpdateDtoResult;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.service.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.util.emum.Status;
import ru.practicum.util.exception.ConflictException;
import ru.practicum.util.service.UnionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.util.emum.State.PUBLISHED;
import static ru.practicum.util.emum.Status.*;

@Slf4j
@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UnionService unionService;

    @Override
    public RequestDto createRequest(Long userId, Long eventId) {

        User user = unionService.getUserOrNotFound(userId);
        Event event = unionService.getEventOrNotFound(eventId);

        Request request = Request.builder()
                .requester(user)
                .event(event)
                .created(LocalDateTime.now())
                .status(PENDING)
                .build();

        Request savedRequest;

        if (requestRepository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ConflictException(String.format("Вы уже подали заявку на участие в мероприятии под id %s",
                    event.getId()));
        }

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException(String.format("Создатель под id %s не может отправить запрос на участие " +
                    "в своем мероприятии", user.getId()));
        }

        if (event.getState() != PUBLISHED) {
            throw new ConflictException(String.format("Событие под id %s не было опубликовано, " +
                    "вы не можете запросить участие", eventId));

        }

        if (event.getParticipantLimit() <= event.getConfirmedRequests() && event.getParticipantLimit() != 0) {
            throw new ConflictException(String.format("Превышен лимит участников события с id %s", event.getId()));
        }

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(CONFIRMED);
            savedRequest = requestRepository.save(request);
            event.setConfirmedRequests(savedRequest.getId());
            eventRepository.save(event);
            return RequestMapper.requestToRequestDto(savedRequest);
        }
        savedRequest = request;
        return RequestMapper.requestToRequestDto(requestRepository.save(savedRequest));
    }

    @Override
    public List<RequestDto> getRequestsByUserId(Long userId) {

        unionService.getUserOrNotFound(userId);
        List<Request> requestList = requestRepository.findByRequesterId(userId);

        return requestList.stream()
                .map(RequestMapper::requestToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {

        unionService.getUserOrNotFound(userId);
        Request request = unionService.getRequestOrNotFound(requestId);
        request.setStatus(Status.CANCELED);

        return RequestMapper.requestToRequestDto(requestRepository.save(request));
    }

    @Override
    public List<RequestDto> getRequestsForEventIdByUserId(Long userId, Long eventId) {
        User user = unionService.getUserOrNotFound(userId);
        Event event = unionService.getEventOrNotFound(eventId);

        unionService.userIsEventCreator(user, event);

        List<Request> requestList = requestRepository.findByEventId(eventId);

        return requestList.stream()
                .map(RequestMapper::requestToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestUpdateDtoResult updateStatusRequestsForEventIdByUserId(RequestUpdateDtoRequest requestDto, Long userId, Long eventId) {

        User user = unionService.getUserOrNotFound(userId);
        Event event = unionService.getEventOrNotFound(eventId);

        RequestUpdateDtoResult result = RequestUpdateDtoResult.builder()
                .confirmedRequests(Collections.emptyList())
                .rejectedRequests(Collections.emptyList())
                .build();

        unionService.userIsEventCreator(user, event);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            List<Request> confirmedRequests = requestRepository.findByStatusAndIds(Status.CONFIRMED, requestDto.getRequestIds());
            result.setConfirmedRequests(confirmedRequests.stream()
                    .map(RequestMapper::requestToRequestDto)
                    .collect(Collectors.toList()));
            return result;
        }

        if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new ConflictException(String.format("Превышен лимит участников события с id %s", event.getId()));
        }

        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();

        long vacantPlace = event.getParticipantLimit() - event.getConfirmedRequests();

        List<Request> requestsList = requestRepository.findAllById(requestDto.getRequestIds());

        for (Request request : requestsList) {
            if (!request.getStatus().equals(PENDING)) {
                throw new ConflictException("Запрос должен иметь статус PENDING");
            } else {
                if (requestDto.getStatus().equals(CONFIRMED)) {
                    request.setStatus(requestDto.getStatus());
                    requestRepository.save(request);
                    event.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(eventId, Status.CONFIRMED));
                    eventRepository.save(event);
                    confirmedRequests.add(request);
                    vacantPlace--;
                } else if (requestDto.getStatus().equals(REJECTED)) {
                    request.setStatus(requestDto.getStatus());
                    requestRepository.save(request);
                    rejectedRequests.add(request);
                } else {
                    request.setStatus(requestDto.getStatus());
                    requestRepository.save(request);
                }
            }
        }

        result.setConfirmedRequests(confirmedRequests.stream()
                .map(RequestMapper::requestToRequestDto)
                .collect(Collectors.toList()));
        result.setRejectedRequests(rejectedRequests.stream()
                .map(RequestMapper::requestToRequestDto)
                .collect(Collectors.toList()));

        eventRepository.save(event);
        requestRepository.saveAll(requestsList);

        return result;
    }
}


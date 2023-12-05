package ru.practicum.request.service;

import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestUpdateDtoRequest;
import ru.practicum.request.dto.RequestUpdateDtoResult;

import java.util.List;

public interface RequestService {

    RequestDto createRequest(Long userId, Long eventId);

    List<RequestDto> getRequestsByUserId(Long userId);

    RequestDto cancelRequest(Long userId, Long requestId);

    List<RequestDto> getRequestsForEventIdByUserId(Long userId, Long eventId);

    RequestUpdateDtoResult updateStatusRequestsForEventIdByUserId(RequestUpdateDtoRequest requestDto, Long userId, Long eventId);
}
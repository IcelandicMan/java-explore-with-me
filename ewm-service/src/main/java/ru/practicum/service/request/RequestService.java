package ru.practicum.service.request;

import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestUpdateDtoRequest;
import ru.practicum.dto.request.RequestUpdateDtoResult;

import java.util.List;

public interface RequestService {

    RequestDto createRequest(Long userId, Long eventId);

    List<RequestDto> getRequestsByUserId(Long userId);

    RequestDto cancelRequest(Long userId, Long requestId);

    List<RequestDto> getRequestsForEventIdByUserId(Long userId, Long eventId);

    RequestUpdateDtoResult updateStatusRequestsForEventIdByUserId(RequestUpdateDtoRequest requestDto, Long userId, Long eventId);
}
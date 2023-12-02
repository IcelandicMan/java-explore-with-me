package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.util.emums.Status;

import java.time.LocalDateTime;

@UtilityClass
public class RequestMapper {
    public RequestDto requestToRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    public Request requestDtoToRequest(RequestDto requestDto, Event event, User user) {
        return Request.builder()
                .id(requestDto.getId())
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(Status.PENDING)
                .build();
    }
}

package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.category.CategoryResponseDto;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.user.UserResponseShortDto;
import ru.practicum.util.emums.State;

import java.time.LocalDateTime;

import static ru.practicum.util.Util.DATE_FORMAT;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class EventResponseFullDto {


    private String annotation;

    private CategoryResponseDto category;

    private Long confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime eventDate;

    private Long id;

    private UserResponseShortDto initiator;

    private LocationDto location;

    private Boolean paid;

    private Long participantLimit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private State state;

    private String title;

    private Long views;
}


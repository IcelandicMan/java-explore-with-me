package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.dto.CategoryResponseDto;
import ru.practicum.user.dto.UserResponseShortDto;

import java.time.LocalDateTime;

import static ru.practicum.util.Util.DATE_FORMAT;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventResponseShortDto {

    private String annotation;

    private CategoryResponseDto category;

    Long confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime eventDate;

    private Long id;

    private UserResponseShortDto initiator;

    private Boolean paid;

    private String title;

    private Long views;
}


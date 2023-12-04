package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.location.dto.LocationDto;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

import static ru.practicum.util.Util.DATE_FORMAT;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestDto {

    @NotBlank(message = "annotation не может быть пустой или состоять из одних лишь пробелов")
    @Size(min = 20, max = 2000, message = "annotation должно быть не менее 20 символов, но не более 2000")
    private String annotation;

    @NotNull
    private Long category;

    @NotBlank(message = "description не может быть пустой или состоять из одних лишь пробелов")
    @Size(min = 20, max = 7000, message = "description должно быть не менее 20 символов, но не более 2000")
    private String description;

    @NotNull
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime eventDate;

    @NotNull
    private LocationDto location;

    @NotNull
    @Builder.Default
    private Boolean paid = false;

    @Builder.Default
    private Long participantLimit = 0L;

    @Builder.Default
    private Boolean requestModeration = true;

    @NotBlank(message = "title не может быть пустой или состоять из одних лишь пробелов")
    @Size(min = 3, max = 120, message = "title должно быть не менее 3 символов, но не более 120")
    private String title;

    @AssertTrue(message = "Дата и время на которые намечено событие не может быть раньше, " +
            "чем через два часа от текущего момента")
    private boolean isValidEventDate() {
        return eventDate.isAfter(LocalDateTime.now().plusHours(2)) || eventDate.isEqual(LocalDateTime.now().plusHours(2));
    }
}


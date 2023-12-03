package ru.practicum.event.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.util.OptionalLocalDateTimeDeserializer;
import ru.practicum.util.annotation.OptionalFutureOrPresent;
import ru.practicum.util.annotation.OptionalStringSize;
import ru.practicum.util.emum.StateAction;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestUpdateDto {

    @OptionalStringSize(min = 20, max = 2000, message = "annotation должно быть не менее 20 символов, но не более 2000")
    private Optional<String> annotation = Optional.empty();

    private Long category;

    @OptionalStringSize(min = 20, max = 7000, message = "description должно быть не менее 20 символов, но не более 2000")
    private Optional<String> description = Optional.empty();

    @JsonDeserialize(using = OptionalLocalDateTimeDeserializer.class)
    @OptionalFutureOrPresent
    private Optional<LocalDateTime> eventDate = Optional.empty();

    private LocationDto location;

    private Boolean paid = false;

    private Long participantLimit;

    private Boolean requestModeration;

    private StateAction stateAction;

    @OptionalStringSize(min = 3, max = 120, message = "title должно быть не менее 3 символов, но не более 120")
    private Optional<String> title = Optional.empty();
}

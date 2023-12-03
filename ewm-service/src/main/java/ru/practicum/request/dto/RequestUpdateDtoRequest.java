package ru.practicum.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.util.emum.Status;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestUpdateDtoRequest {

    List<Long> requestIds;

    Status status;
}
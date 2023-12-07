package ru.practicum.comment.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.util.deserializer.OptionalLongDeserializer;
import ru.practicum.util.emum.State;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {

    @NotBlank(message = "text не может быть пустой или состоять из одних лишь пробелов")
    @Size(min = 2, max = 500, message = "annotation должно быть не менее 5 символов, но не более 500")
    private String text;

    private State state;

    @JsonDeserialize(using = OptionalLongDeserializer.class)
    private Optional<Long> answerToCommentId = Optional.empty();
}

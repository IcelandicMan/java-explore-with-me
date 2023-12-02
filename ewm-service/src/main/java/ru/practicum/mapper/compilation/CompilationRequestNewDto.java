package ru.practicum.mapper.compilation;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
public class CompilationRequestNewDto {

    Set<Long> events;

    Boolean pinned;

    @NotBlank
    @Size(min = 1, max = 50)
    String title;
}

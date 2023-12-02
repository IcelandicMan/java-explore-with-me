package ru.practicum.dto.category;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class CategoryRequestDto {

    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(max = 50, message = "Название не должно превышать 50 символами")
    private String name;

}

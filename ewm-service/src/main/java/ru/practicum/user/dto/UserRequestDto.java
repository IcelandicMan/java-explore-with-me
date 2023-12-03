package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserRequestDto {

    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 250, message = "Имя должно быть не менее 2 символов, но не более 250")
    private String name;

    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Некорректный формат электронной почты")
    @Size(min = 6, max = 254, message = "Адрес электронной почты должен быть не менее 6 символов, но не более 255")
    private String email;
}


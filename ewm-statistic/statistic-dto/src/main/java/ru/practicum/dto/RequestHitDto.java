package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class RequestHitDto {

    @NotBlank
    @Size(max = 255, message = "Максимальная длина наименования приложения превышена")
    private String app;

    @NotBlank
    @Size(max = 255, message = "Максимальная длина URI адреса превышена")
    private String uri;

    @NotBlank
    @Size(max = 15, message = "Максимальная длина ip адреса не может превышать 15 символов")
    private String ip;

    @NotNull
    private String timestamp;
}


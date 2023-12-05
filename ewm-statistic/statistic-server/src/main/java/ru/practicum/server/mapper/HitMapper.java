package ru.practicum.server.mapper;

import ru.practicum.dto.RequestHitDto;
import ru.practicum.dto.ResponseHitDto;
import ru.practicum.server.model.HitEntity;

import java.time.LocalDateTime;

import static ru.practicum.dto.Constants.DATE_TIME_FORMATTER;


public class HitMapper {

    public static HitEntity requestHitDtoToHitEntity(RequestHitDto requestHitDto) {
        return HitEntity.builder()
                .app(requestHitDto.getApp())
                .uri(requestHitDto.getUri())
                .ip(requestHitDto.getIp())
                .timestamp(LocalDateTime.parse(requestHitDto.getTimestamp(),
                        DATE_TIME_FORMATTER))
                .build();
    }

    public static ResponseHitDto hitEntitytoResponseHitDto(HitEntity hitEntity) {
        return ResponseHitDto.builder()
                .app(hitEntity.getApp())
                .uri(hitEntity.getUri())
                .build();
    }
}

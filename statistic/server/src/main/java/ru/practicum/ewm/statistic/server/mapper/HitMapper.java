package ru.practicum.ewm.statistic.server.mapper;

import ru.practicum.ewm.statistic.dto.RequestHitDto;
import ru.practicum.ewm.statistic.server.model.HitEntity;

import java.time.LocalDateTime;

import static ru.practicum.ewm.statistic.dto.Constants.DATE_TIME_FORMATTER;

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
}

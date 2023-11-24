package ru.practicum.ewm.statistic.server.mapper;

import ru.practicum.ewm.statistic.server.model.HitEntity;
import ru.practicum.ewm.statistic.dto.*;

import java.time.LocalDateTime;

public class HitMapper {

    public static HitEntity RequestHitDtoToHitEntity(RequestHitDto requestHitDto) {
        return HitEntity.builder()
                .app(requestHitDto.getApp())
                .uri(requestHitDto.getUri())
                .ip(requestHitDto.getIp())
                .timestamp(requestHitDto.getTimestamp())
                .build();
    }
}

package ru.practicum.ewm.statistic.server.mapper;

import ru.practicum.ewm.statistic.server.model.HitEntity;
import ru.practicum.ewm.statistic.dto.*;

public class HitMapper {

    public static HitEntity requestHitDtoToHitEntity(RequestHitDto requestHitDto) {
        return HitEntity.builder()
                .app(requestHitDto.getApp())
                .uri(requestHitDto.getUri())
                .ip(requestHitDto.getIp())
                .timestamp(requestHitDto.getTimestamp())
                .build();
    }
}

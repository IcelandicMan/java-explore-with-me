package ru.practicum.ewm.statistic.server.service;

import ru.practicum.ewm.statistic.dto.RequestHitDto;
import ru.practicum.ewm.statistic.dto.ResponseHitDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {

    void createHit(RequestHitDto hitDto);

    List<ResponseHitDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}

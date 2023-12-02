package ru.practicum.server.service;

import ru.practicum.dto.RequestHitDto;
import ru.practicum.dto.ResponseHitDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {

    ResponseHitDto createHit(RequestHitDto hitDto);

    List<ResponseHitDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}

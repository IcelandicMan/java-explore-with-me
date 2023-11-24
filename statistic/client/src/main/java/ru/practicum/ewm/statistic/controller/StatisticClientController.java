package ru.practicum.ewm.statistic.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.statistic.client.StatisticClient;
import ru.practicum.ewm.statistic.dto.RequestHitDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
public class StatisticClientController {
    private final StatisticClient client;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createHit(@Valid @RequestBody RequestHitDto requestHitDto) {
        log.info("Запрошено создание записи статистики посещения от пользователя с ip: {}, " +
                        "для приложения: {}, " + "адреса по Uri: {} ",
                requestHitDto.getIp(), requestHitDto.getApp(), requestHitDto.getUri());
        return client.postHit(requestHitDto);
    }

    public ResponseEntity<Object> getStats(
            @RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(name = "uris", required = false) List<String> uris,
            @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        log.info("Запрошена статистика посещения uris: {}, " +
                        "за период с: {}, по: {}, с фильтрацией по уникальным посещениям: {} ",
                uris, start, end, unique);
        return client.getStats(start, end, uris, unique);
    }
}

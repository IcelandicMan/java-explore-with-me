package ru.practicum.ewm.statistic.server.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.statistic.dto.RequestHitDto;
import ru.practicum.ewm.statistic.dto.ResponseHitDto;
import ru.practicum.ewm.statistic.server.service.StatisticService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.statistic.dto.Constants.DATE_TIME_FORMAT;

@Slf4j
@RestController
@AllArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;


    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHit(@RequestBody RequestHitDto hitDto) {
        log.info("Создание записи статистики: " + hitDto.toString());
        statisticService.createHit(hitDto);
    }

    @GetMapping("/stats")
    public List<ResponseHitDto> getStats(@RequestParam(name = "start") @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime start,
                                         @RequestParam(name = "end") @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime end,
                                         @RequestParam(name = "uris", required = false) List<String> uris,
                                         @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        log.info("Запрос статистики по следующим данным: начало {}, конец: {}, uri: {}, поиск по уникальным ip: {}",
                start.toString(), end.toString(), uris != null ? uris.toString() : "null", unique);
        return statisticService.getStats(start, end, uris, unique);
    }
}


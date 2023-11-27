package ru.practicum.ewm.statistic.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.statistic.dto.RequestHitDto;
import ru.practicum.ewm.statistic.dto.ResponseHitDto;
import ru.practicum.ewm.statistic.server.mapper.HitMapper;
import ru.practicum.ewm.statistic.server.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository repository;

    @Transactional
    @Override
    public void createHit(RequestHitDto requestHitDto) {
        repository.save(HitMapper.requestHitDtoToHitEntity(requestHitDto));
    }

    @Override
    public List<ResponseHitDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                log.info("Запрос всей статистики c уникальными пользователями");
                return repository.findAllStatsByUniqueIp(start, end);
            } else {
                log.info("Запрос всей статистики");
                return repository.findAllStats(start, end);
            }
        } else {
            if (unique) {
                log.info("Запрос всей статистики URI адресов c уникальными пользователями");
                return repository.findStatsByUriAndUniqueIp(start, end, uris);
            } else {
                log.info("Запрос всей статистики URI адресов");
                return repository.findStatsByUri(start, end, uris);
            }
        }
    }
}

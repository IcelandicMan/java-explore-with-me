package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.RequestHitDto;
import ru.practicum.dto.ResponseHitDto;
import ru.practicum.server.exeprion.InvalidTimeRangeException;
import ru.practicum.server.mapper.HitMapper;
import ru.practicum.server.model.HitEntity;
import ru.practicum.server.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository repository;

    @Override
    public ResponseHitDto createHit(RequestHitDto requestHitDto) {
        HitEntity hitEntity = HitMapper.requestHitDtoToHitEntity(requestHitDto);
        return HitMapper.hitEntitytoResponseHitDto(repository.save(hitEntity));
    }

    @Override
    public List<ResponseHitDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start.isAfter(end)) {
            throw new InvalidTimeRangeException("Время начала не может быть позднее время окончания");
        }
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

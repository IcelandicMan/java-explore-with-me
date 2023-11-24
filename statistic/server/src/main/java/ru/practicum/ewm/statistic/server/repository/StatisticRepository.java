package ru.practicum.ewm.statistic.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.statistic.dto.ResponseHitDto;
import ru.practicum.ewm.statistic.server.model.HitEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<HitEntity, Long> {

    @Query("SELECT new ru.practicum.ewm.statistic.dto.ResponseHitDto(h.app, h.uri, COUNT(h.ip)) " +
            "FROM HitEntity AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ResponseHitDto> findAllStats(@Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.statistic.dto.ResponseHitDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM HitEntity AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<ResponseHitDto> findAllStatsByUniqueIp(@Param("start") LocalDateTime start,
                                                @Param("end") LocalDateTime end);


    @Query("SELECT new ru.practicum.ewm.statistic.dto.ResponseHitDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM HitEntity AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<ResponseHitDto> findStatsByUriAndUniqueIp(@Param("start") LocalDateTime start,
                                                   @Param("end") LocalDateTime end,
                                                   @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.ewm.statistic.dto.ResponseHitDto(h.app, h.uri, COUNT(h.ip)) " +
            "FROM HitEntity AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ResponseHitDto> findStatsByUri(@Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end,
                                        @Param("uris") List<String> uris);
}
package ru.practicum.stat.repository;

import java.util.List;
import java.time.LocalDateTime;

import ru.practicum.stat.model.Hit;
import ru.practicum.stat.dto.ViewStatDto;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface StatRepository extends JpaRepository<Hit, Long> {


    // уникальный IP, список ссылок есть
    @Query("select new ru.practicum.stat.dto.ViewStatDto (hit.app, hit.uri, count(distinct hit.ip))" +
            "from Hit hit " +
            "where (hit.timeStamp between :start and :end) " +
            "and hit.uri IN (:uris) " +
            "group by hit.app, hit.uri " +
            "order by count(distinct hit.ip) desc")
    List<ViewStatDto> getStatUniqueIpUris(@Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end,
                                          @Param("uris") List<String> uris);




    // // уникальный IP, список ссылок нет
    @Query("select new ru.practicum.stat.dto.ViewStatDto  (hit.app, hit.uri, count(distinct hit.ip))" +
            "from Hit hit " +
            "where (hit.timeStamp between :start and :end) " +
            "group by hit.app, hit.uri " +
            "order by count(distinct hit.ip) desc")
    List<ViewStatDto> getStatUniqueIpNoUris(@Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end);




    // не уникальный IP, список ссылок есть
    @Query("select new ru.practicum.stat.dto.ViewStatDto  (hit.app, hit.uri, count(hit.ip))" +
            "from Hit hit " +
            "where (hit.timeStamp between :start and :end) " +
            "and hit.uri IN (:uris) " +
            "group by hit.app, hit.uri " +
            "order by count(hit.ip) desc")
    List<ViewStatDto> getStatUnUniqueIpUris(@Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end,
                                            @Param("uris") List<String> uris);

    // не уникальный IP, список ссылок нет
    @Query("select new ru.practicum.stat.dto.ViewStatDto  (hit.app, hit.uri, count(hit.ip))" +
            "from Hit hit " +
            "where (hit.timeStamp between :start and :end) " +
            "group by hit.app, hit.uri " +
            "order by count(hit.ip) desc")
    List<ViewStatDto> getStatUnUniqueIpNoUris(@Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end);

    int countAllBy();
}
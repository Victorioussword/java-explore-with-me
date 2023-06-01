package ru.practicum.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.practicum.stat.model.Hit;
import org.springframework.stereotype.Repository;
import ru.practicum.stat.model.ViewStat;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Hit, Long> {


    @Query("select new ru.practicum.stat.model.ViewStat (hit.app, hit.uri, count(distinct hit.ip))" +
            "from Hit hit " +
            "where (hit.timestamp between :start and :end) " +
            "and hit.uri IN (:uris) " +
            "group by hit.app, hit.uri " +
            "order by count(distinct hit.ip) desc")
    List<ViewStat> getStatUniqueIpUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);


    @Query("select new ru.practicum.stat.model.ViewStat (hit.app, hit.uri, count(distinct hit.ip))" +
            "from Hit hit " +
            "where (hit.timestamp between :start and :end) " +
            "group by hit.app, hit.uri " +
            "order by count(distinct hit.ip) desc")
    List<ViewStat> getStatUniqueIpNoUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


    @Query("select new ru.practicum.stat.model.ViewStat (hit.app, hit.uri, count(hit.ip))" +
            "from Hit hit " +
            "where (hit.timestamp between :start and :end) " +
            "and hit.uri IN (:uris) " +
            "group by hit.app, hit.uri " +
            "order by count(hit.ip) desc")
    List<ViewStat> getStatUnUniqueIpUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);


    @Query("select new ru.practicum.stat.model.ViewStat (hit.app, hit.uri, count(hit.ip))" +
            "from Hit hit " +
            "where (hit.timestamp between :start and :end) " +
            "group by hit.app, hit.uri " +
            "order by count(hit.ip) desc")
    List<ViewStat> getStatUnUniqueIpNoUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}

//    String app;
//    String uri;
//    String hits;

//public class Hit {
//    Long id;
//    String app;
//    String uri;
//    String ip;
//    LocalDateTime timestamp;

//@Query("select booking from Booking booking " +
//        "where booking.start < ?2 " +
//        "and booking.end > ?2 " +
//        "and booking.item.owner = ?1 " +
//        "order by booking.start desc")

//@Query("select i " +
//        "from Item i " +
//        "where (lower(i.name) like lower(concat('%', ?1, '%')) " +
//        "or lower(i.description) like lower(concat('%', ?1, '%'))) " +
//        "and i.available is true ")
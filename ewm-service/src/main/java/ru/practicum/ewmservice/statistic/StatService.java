package ru.practicum.ewmservice.statistic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import ru.practicum.stat.client.StatClient;
import ru.practicum.stat.dto.InputHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatService {

//    private final StatClient statClient;

    public ResponseEntity<Object> getStat(String start,
                                          String end,
                                          List<String> uris,
                                          Boolean unique) {

        LocalDateTime start1 = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime end1 = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("StatsService - getStat()");
//        return statClient.getStat(start1, end1, uris, unique);
        return null;
    }

    @Transactional
    public void view(InputHitDto inputHitDto) {
        log.info("StatService - view() input - {}", inputHitDto);
//        statClient.postHit(inputHitDto);
    }
}

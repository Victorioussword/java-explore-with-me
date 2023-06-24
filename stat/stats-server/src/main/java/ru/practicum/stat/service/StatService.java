package ru.practicum.stat.service;

import java.util.List;
import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.stat.model.Hit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import ru.practicum.stat.repository.StatRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatService {
    private final StatRepository statRepository;

    public Hit postHit(Hit hit) {

        log.info("StatService - addHit(). ДОбавлен Hit {}", hit.toString());

        return statRepository.save(hit);
    }

    public ResponseEntity getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        if (start.isAfter(end)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (unique && uris != null) {
            log.info("StatService - getStat(). Start {}, ENd {}, size {}, unique {}", start, end, uris.size(), unique);
            return ResponseEntity.status(HttpStatus.OK).body(statRepository.getStatUniqueIpUris(start, end, uris));  // уникальный IP, список ссылок есть
        } else if (unique && uris == null) {
            log.info("StatService - getStat(). Start {}, ENd {}, size = 0, unique {}", start, end, unique);
            return ResponseEntity.status(HttpStatus.OK).body(statRepository.getStatUniqueIpNoUris(start, end));  // // уникальный IP, список ссылок нет
        } else if (!unique && uris != null) {
            log.info("StatService - getStat(). Start {}, ENd {}, size {}, unique {}", start, end, uris.size(), unique);
            return ResponseEntity.status(HttpStatus.OK).body(statRepository.getStatUnUniqueIpUris(start, end, uris));  // не уникальный IP, список ссылок есть
        } else {
            log.info("StatService - getStat(). Start {}, ENd {}, size = 0, unique {}", start, end, unique);
            return ResponseEntity.status(HttpStatus.OK).body(statRepository.getStatUnUniqueIpNoUris(start, end));  // не уникальный IP, список ссылок нет
        }
    }
}

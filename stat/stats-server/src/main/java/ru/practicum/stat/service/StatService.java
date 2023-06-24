package ru.practicum.stat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.stat.dto.ViewStatDto;
import org.springframework.stereotype.Service;
import ru.practicum.stat.model.Hit;
import ru.practicum.stat.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatService {
    private final StatRepository statRepository;

    public Hit postHit(Hit hit) {

        log.info("StatService - addHit(). ДОбавлен Hit {}", hit.toString());

        return statRepository.save(hit);
    }

    public List<ViewStatDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        if (unique && uris != null) {
            log.info("StatService - getStat(). Start {}, ENd {}, size {}, unique {}", start, end, uris.size(), unique);
            return statRepository.getStatUniqueIpUris(start, end, uris);  // уникальный IP, список ссылок есть
        } else if (unique && uris == null) {
            log.info("StatService - getStat(). Start {}, ENd {}, size = 0, unique {}", start, end, unique);
            return statRepository.getStatUniqueIpNoUris(start, end);  // // уникальный IP, список ссылок нет
        } else if (!unique && uris != null) {
            log.info("StatService - getStat(). Start {}, ENd {}, size {}, unique {}", start, end, uris.size(), unique);
            return statRepository.getStatUnUniqueIpUris(start, end, uris);  // не уникальный IP, список ссылок есть
        } else {
            log.info("StatService - getStat(). Start {}, ENd {}, size = 0, unique {}", start, end, unique);
            return statRepository.getStatUnUniqueIpNoUris(start, end);  // не уникальный IP, список ссылок нет

        }
    }
}

package ru.practicum.stat.service;

import java.util.List;
import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.stat.dto.ViewStatDto;
import ru.practicum.stat.exceptions.BadRequestException;
import ru.practicum.stat.model.Hit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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


    public int getQuantityLog() {
        return statRepository.countAllBy();
    }


    public List<ViewStatDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        log.info("StatService - getViewStats(). Получены даты {} и  {}", start.toString(), end.toString());

        if (start == null || end == null || start.isAfter(end)) {
            throw new BadRequestException("Start после End");
        }


        if (unique && uris != null) {
            log.info("StatService - getStat(). Переход в Repository. Start {}, ENd {}, size {}, unique {}", start, end, uris.size(), unique);
            return statRepository.getStatUniqueIpUris(start, end, uris);  // уникальный IP, список ссылок есть

        } else if (unique && uris == null) {
            log.info("StatService - getStat(). Переход в Repository. Start {}, ENd {}, size = 0, unique {}", start, end, unique);
            return statRepository.getStatUniqueIpNoUris(start, end);  // // уникальный IP, список ссылок нет
        } else if (!unique && uris != null) {
            log.info("StatService - getStat(). Переход в Repository. Start {}, ENd {}, size {}, unique {}", start, end, uris.size(), unique);
            return statRepository.getStatUnUniqueIpUris(start, end, uris);  // не уникальный IP, список ссылок есть
        } else {
            log.info("StatService - getStat(). Переход в Repository. Start {}, ENd {}, size = 0, unique {}", start, end, unique);
            return statRepository.getStatUnUniqueIpNoUris(start, end);  // не уникальный IP, список ссылок нет
        }
    }
}

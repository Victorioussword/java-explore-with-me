package ru.practicum.stat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stat.dto.InputHitDto;
import ru.practicum.stat.dto.OutputHitDto;
import ru.practicum.stat.model.ViewStat;
import ru.practicum.stat.mapper.HitMapper;
import ru.practicum.stat.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatService {

    private final StatRepository statRepository;

    public OutputHitDto postHit(InputHitDto inputHitDto) {
        log.info("StatService - postHit(). Создан {}", inputHitDto.toString());
        return HitMapper.toOutputHitDto(statRepository.save(HitMapper.toHit(inputHitDto)));
    }

    public List<ViewStat> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

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

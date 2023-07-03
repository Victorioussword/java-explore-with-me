package ru.practicum.stat.controller;

import java.util.List;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.stat.dto.HitDto;
import lombok.RequiredArgsConstructor;
import ru.practicum.stat.mapper.HitMapper;
import org.springframework.http.HttpStatus;
import ru.practicum.stat.service.StatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatController {
    private final StatService statsService;


    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto postHit(@RequestBody HitDto hitDto) {

        log.info("Количество записей в б.д. до сохранения Hit{}", statsService.getQuantityLog());
        log.info("StatService - addHit(). ДОбавлен Hit {}", hitDto.toString());
                return HitMapper.toHitDto(statsService.postHit(HitMapper.toHit(hitDto)));
    }


    @GetMapping(value = "/stats")
    public ResponseEntity getViewStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                       @RequestParam(required = false) List<String> uris,
                                       @RequestParam(defaultValue = "false") boolean unique) {

        log.info("StatController - getViewStats(). Получены даты {} и  {}", start.toString(), end.toString());

        log.info("Переход в Service");
        return ResponseEntity.status(HttpStatus.OK).body(statsService.getViewStats(
                start,
                end,
                uris,
                unique));
    }
}
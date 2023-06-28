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

        log.info("StatService - addHit(). ДОбавлен Hit {}", hitDto.toString());

        return HitMapper.toHitDto(statsService.postHit(HitMapper.toHit(hitDto)));
    }

    @GetMapping(value = "/stats")
    public ResponseEntity getViewStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,   //todo @Notnull
                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,     //todo @Notnull
                                       @RequestParam(required = false) List<String> uris,
                                       @RequestParam(defaultValue = "false") boolean unique) {

        if (end.isBefore(start)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(statsService.getViewStats(start, end, uris, unique));
    }
}
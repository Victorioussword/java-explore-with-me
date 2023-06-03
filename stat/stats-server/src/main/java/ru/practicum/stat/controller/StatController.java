package ru.practicum.stat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat.dto.InputHitDto;
import ru.practicum.stat.dto.OutputHitDto;
import ru.practicum.stat.model.ViewStat;
import ru.practicum.stat.service.StatService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class StatController {
    private final StatService statService;
    private static final String FORMATTER  = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("/hit")
    public OutputHitDto postHit(@Valid @RequestBody InputHitDto inputHitDto) {
        log.info("StatController - postHit(). Создан {}", inputHitDto.toString());
        return statService.postHit(inputHitDto);
    }


    @GetMapping("/stats")
    public List<ViewStat> geStat(
            @RequestParam @DateTimeFormat(pattern = FORMATTER) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = FORMATTER) LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique
    ) {
        log.info("StatController - geStat()");
        return statService.getStat(start, end, uris, unique);
    }
}

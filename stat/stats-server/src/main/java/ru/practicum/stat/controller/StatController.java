package ru.practicum.stat.controller;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.stat.dto.HitDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat.dto.ViewStatDto;
import ru.practicum.stat.mapper.HitMapper;
import ru.practicum.stat.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

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
    public List<ViewStatDto> getViewStats(@RequestParam @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                          @RequestParam @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                          @RequestParam(required = false) List<String> uris,
                                          @RequestParam(defaultValue = "false") boolean unique) {

      //  ResponseEntity re = ResponseEntity.status(HttpStatus.OK).body(statsService.getViewStats(start, end, uris, unique));

 //       if (uris != null){
 //           log.info("StatService - getViewStats(). uris {}", uris.toString());
//        }
            // StatService - getViewStats(). отпралено <200 OK OK,<200 OK OK,[],[]>,[]>

 //           log.info("StatService - getViewStats(). отпралено {}", re.toString());
 //       log.info("StatService - getViewStats(). Body {}", re.getBody().toString());

        return statsService.getViewStats(start, end, uris, unique);
    }
}

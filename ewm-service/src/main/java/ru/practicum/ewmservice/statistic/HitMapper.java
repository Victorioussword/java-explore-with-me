package ru.practicum.ewmservice.statistic;

import ru.practicum.stat.dto.OutputHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

public class HitMapper {

    public static OutputHitDto toOutputHitDto(String app, HttpServletRequest request) {
        return new OutputHitDto(null,
                app,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now());
    }
}

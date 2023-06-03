package ru.practicum.stat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@Setter
@ToString
public class OutputHitDto {

    private Long id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}

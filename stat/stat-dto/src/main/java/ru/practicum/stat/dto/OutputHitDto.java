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

    Long id;
    String app;
    String uri;
    String ip;
    LocalDateTime timestamp;
}

package ru.practicum.stat.dto;

import lombok.*;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;


@Setter  // todo Было @DATA
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HitDto {

    private Long id;

    @NotBlank
    private String app;

    @NotBlank
    private String uri;

    @NotBlank
    private String ip;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timeStamp;  // TODO private
}

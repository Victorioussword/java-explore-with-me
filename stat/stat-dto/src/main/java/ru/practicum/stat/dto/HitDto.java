package ru.practicum.stat.dto;

import lombok.*;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;


@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HitDto {

//{
// "app":"ewm-main-service",
// "uri":"/events/96",
// "ip":"106.154.151.141",
// "timestamp":"2023-06-28 12:14:39"
// }

    private Long id;

    @NotBlank
    private String app;

    @NotBlank
    private String uri;

    @NotBlank
    private String ip;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}

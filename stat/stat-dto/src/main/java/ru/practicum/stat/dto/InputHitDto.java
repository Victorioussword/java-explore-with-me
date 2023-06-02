package ru.practicum.stat.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class InputHitDto {


    @NotNull
    @NotBlank
    private String app;

    @NotNull
    @NotBlank
    private String uri;

    @NotNull
    @NotBlank
    private String ip;

    @NotNull
    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
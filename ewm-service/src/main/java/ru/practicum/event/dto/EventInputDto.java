package ru.practicum.event.dto;

import lombok.*;

import java.time.LocalDateTime;
import javax.persistence.Column;

import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;

import ru.practicum.event.model.Location;

import com.fasterxml.jackson.annotation.JsonFormat;

@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

public class EventInputDto {
    private Long id;

    @NotBlank
    @NotNull
    @Size(max = 2000, min = 20)
    @Column(length = 2000)
    private String annotation;

    @NotNull
    private Long category;

    @NotBlank
    @Size(max = 7000, min = 20)
    @Column(length = 7000)
    private String description;

    @NotNull
    private Location location;

    private boolean paid;

    @PositiveOrZero
    private Long participantLimit;

    private Boolean requestModeration = true;

    @NotNull
    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    @Size(max = 120, min = 3)
    private String title;
}

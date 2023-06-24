package ru.practicum.event.dto;

import lombok.*;

import java.time.LocalDateTime;

import ru.practicum.utils.enums.State;
import lombok.experimental.SuperBuilder;
import ru.practicum.event.model.Location;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.category.model.Category;
import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

public class EventDto {

    private long id;

    private String title;

    private String description;

    private String annotation;

    private Category category;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdOn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;

    private Location location;

    private UserShortDto initiator;

    private Boolean paid;

    private Long participantLimit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private State state;

    private Long views;

    private Long confirmedRequests;
}
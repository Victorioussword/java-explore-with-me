package ru.practicum.ewmservice.event.dto.ForEventContollerUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewmservice.event.model.Location;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventByUserDto {

    private String annotation;

    private Long category;

    private String description;

    private String eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private String title;

    private String stateAction;
}

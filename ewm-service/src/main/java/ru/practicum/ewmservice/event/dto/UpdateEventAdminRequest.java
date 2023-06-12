package ru.practicum.ewmservice.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmservice.event.model.Location;
import ru.practicum.ewmservice.event.model.UserStateAction;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UpdateEventAdminRequest {

    String annotation;

    Long category;

    String description;

    LocalDateTime eventDate;

    Location location;

    Boolean paid;

    @PositiveOrZero
    Integer participantLimit;

    Boolean requestModeration;

    UserStateAction stateAction;

    String title;
}

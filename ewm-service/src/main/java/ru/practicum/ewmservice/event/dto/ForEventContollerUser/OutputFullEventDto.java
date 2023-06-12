package ru.practicum.ewmservice.event.dto.ForEventContollerUser;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewmservice.category.dto.CategoryDto;
import ru.practicum.ewmservice.event.model.Location;
import ru.practicum.ewmservice.event.model.State;
import ru.practicum.ewmservice.user.model.UserShort;

import java.time.LocalDateTime;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OutputFullEventDto {

    private long id;
    private String annotation;
    private CategoryDto category; //=
    private Long confirmedRequests;  //=
    private String description;
    private LocalDateTime eventDate;
    private UserShort initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
    private Long views;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private State state;
}


//"annotation": "Эксклюзивность нашего шоу гарантирует привлечение максимальной зрительской аудитории",
//      "category": {
//        "id": 1,
//        "name": "Концерты"
//      },
//      "confirmedRequests": 5,
//      "eventDate": "2024-03-10 14:30:00",
//      "id": 1,
//      "initiator": {
//        "id": 3,
//        "name": "Фёдоров Матвей"
//      },
//      "paid": true,
//      "title": "Знаменитое шоу 'Летающая кукуруза'",
//      "views": 999
//    },
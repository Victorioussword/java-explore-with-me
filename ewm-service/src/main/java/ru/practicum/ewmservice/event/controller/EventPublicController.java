package ru.practicum.ewmservice.event.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.event.dto.ForEventContollerUser.OutputEventShortDto;
import ru.practicum.ewmservice.event.dto.ForEventContollerUser.OutputFullEventDto;
import ru.practicum.ewmservice.event.model.EventSort;
import ru.practicum.ewmservice.event.service.EventService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class EventPublicController {


    private final EventService eventService;


    @GetMapping("/events")  // ВернО!!!
    public List<OutputEventShortDto> getAllEvents(@RequestParam(required = false) String text,
                                                  @RequestParam(name = "categories", required = false) List<Long> categoryIds,
                                                  @RequestParam(required = false) Boolean paid,
                                                  @RequestParam(required = false) String start,
                                                  @RequestParam(required = false) String end,
                                                  @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                                  @RequestParam(required = false) EventSort sort,
                                                  @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                  @Positive @RequestParam(defaultValue = "10") int size,
                                                  HttpServletRequest request) {
        log.info("EventController - getAllEventsByPublic()");
        return eventService.search(text,
                categoryIds,
                paid,
                start,
                end,
                onlyAvailable,
                sort,
                from,
                size,
                request
        );
    }

    @GetMapping("/events/{id}")  // ВернО!!!
    @ResponseStatus(HttpStatus.OK)
    public OutputFullEventDto getEventByPublic(@PathVariable Long id, HttpServletRequest request) {

        log.info("EventController - getEventByPublic()");
        return eventService.getEventByPublic(id, request);
    }
}

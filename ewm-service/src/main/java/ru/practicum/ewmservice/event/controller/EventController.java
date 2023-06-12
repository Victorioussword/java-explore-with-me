package ru.practicum.ewmservice.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.event.dto.ForEventContollerUser.InputEventDto;
import ru.practicum.ewmservice.event.dto.ForEventContollerUser.OutputEventShortDto;
import ru.practicum.ewmservice.event.dto.ForEventContollerUser.OutputFullEventDto;
import ru.practicum.ewmservice.event.dto.ForEventContollerUser.UpdateEventByUserDto;
import ru.practicum.ewmservice.event.service.EventService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class EventController {

    private final EventService eventService;

    // Спецификация:
    // https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-main-service-spec.json

    /////////////////Private//////////

    @PostMapping("/users/{userId}/events")   // Верно!! 2
    public OutputFullEventDto createEvent(@RequestBody InputEventDto inputEventDto,
                                          @PathVariable Long userId) {

        log.info("EventController - createEvent(). Создан {}", inputEventDto.toString());
        return eventService.createEvent(inputEventDto, userId);
    }

    @GetMapping("/users/{userId}/events")  // Верно!!
    public List<OutputEventShortDto> getEventsByUser(@PathVariable long userId,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        log.info("EventController - getEventsByUser(). Отправлен список ");
        return eventService.getEventsByUser(userId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")  //Верно!!
    public OutputFullEventDto getEventByIdAndUser(@PathVariable long userId, @PathVariable long eventId) {
        log.info("EventController - getEventByIdAndUser()");
        return eventService.getEventByIdAndUser(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")   //Верно!!  // todo - должна отдаваться какая то хрень
    public OutputFullEventDto update(@PathVariable long userId,
                                     @PathVariable long eventId,
                                     @Valid @RequestBody UpdateEventByUserDto updateEventByUserDto) {
        log.info("EventController - update()");
        return eventService.updateEvent(userId, eventId, updateEventByUserDto);
    }
}

//  Private: События
//    get   /users/{userId}/events
//    post  /users/{userId}/events
//    get   /users/{userId}/events/{eventId}
//    patch   /users/{userId}/events/{eventId}
//    get   /users/{userId}/events/{eventId}/requests
//    patch   /users/{usrId}/events/{eventId}/requests

//  Admin: События
//    get      /admin/events
//    patch    /admin/events/{eventId}


//  Public: События
//     get   /events
//     get   /events/{id}
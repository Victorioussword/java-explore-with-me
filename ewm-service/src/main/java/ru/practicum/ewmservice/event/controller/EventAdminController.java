package ru.practicum.ewmservice.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.event.dto.ForEventContollerAdm.UpdateEventByAdminDto;
import ru.practicum.ewmservice.event.dto.ForEventContollerUser.OutputFullEventDto;
import ru.practicum.ewmservice.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class EventAdminController {

    private final EventService eventService;

    @GetMapping("/admin/events")  // Верно!
    public List<OutputFullEventDto> getAllEventsByAdmin(@RequestParam(required = false) List<Long> users,
                                                        @RequestParam(required = false) List<String> states,
                                                        @RequestParam(required = false) List<Long> categories,
                                                        @RequestParam(required = false) String rangeStart,
                                                        @RequestParam(required = false) String rangeEnd,
                                                        @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
                                                        @RequestParam(required = false, defaultValue = "10") @Positive int size) {
        log.info("EventController - getEventsByAdmin()");
        return eventService.getEventsByAdmin2(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/admin/events/{eventId}") // ВернО!!!
    public OutputFullEventDto updateEventByAdmin(@PathVariable Long eventId,
                                                 @Valid @RequestBody UpdateEventByAdminDto updateEventByAdminDto) {
        log.info("EventController - patchByAdmin()");
        //  return null;  // todo Разобраться с парамтрами
        return eventService.updateEventByAdmin(eventId, updateEventByAdminDto);
    }

}

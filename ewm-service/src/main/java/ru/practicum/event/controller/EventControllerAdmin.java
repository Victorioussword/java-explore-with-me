package ru.practicum.event.controller;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.event.dto.EventDto;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventUpdateByAdminDto;
import ru.practicum.event.service.EventServiceAdmin;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")

public class EventControllerAdmin {

    private final EventServiceAdmin eventServiceAdmin;


    @GetMapping
    public List<EventDto> getEventsByAdmin(

            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,      // todo - теперь принимаем время
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,         // todo - теперь принимаем время
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) List<String> states,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        List<EventDto> list = eventServiceAdmin.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
        log.info("EventControllerAdmin - getEventsByAdmin().  Возвращено   {} событий. ", list.size());
        return list;
    }


    @PatchMapping("/{eventId}")
    public EventDto updateEvent(
            @PathVariable Long eventId,
            @RequestBody @Valid EventUpdateByAdminDto eventUpdateByAdminDto) {

        EventDto eventDto = eventServiceAdmin.updateEvent(eventUpdateByAdminDto, eventId);

        log.info("EventControllerAdmin - updateEvent().  Обновлено   {} . ", eventDto.toString());

        return eventDto;
    }
}
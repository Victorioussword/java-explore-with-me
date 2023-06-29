package ru.practicum.event.controller;


import java.util.List;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.practicum.event.dto.EventDto;
import ru.practicum.request.dto.RequestDto;
import org.springframework.http.HttpStatus;
import ru.practicum.event.dto.EventInputDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.request.dto.RequestsListsDto;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventUpdateByUserDto;
import ru.practicum.event.service.EventServicePrivate;
import ru.practicum.request.dto.RequestUpdateStatusDto;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")

public class EventControllerPrivate {

    private final EventServicePrivate eventServicePrivate;

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<RequestDto> getRequestsForEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId) {

        List<RequestDto> list = eventServicePrivate.getRequestsForEvent(userId, eventId);
        log.info("EventControllerPrivate  - getRequestsForEvent.  Возвращен список из {} элементов", list.size());
        return list;
    }


    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto create(
            @RequestBody @Valid EventInputDto dto,
            @PathVariable Long userId) {

        EventDto eventDto = eventServicePrivate.create(dto, userId);
        log.info(" EventControllerPrivate  - create().  Создано   {}. ", eventDto.toString());
        return eventDto;
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEventsByUser(
            @PathVariable Long userId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        List<EventShortDto> list = eventServicePrivate.getEventsByUser(userId, from, size);
        log.info(" EventControllerPrivate  - getEventsByUser().  Возвращен список из    {} элементов. ", list.size());
        return list;
    }


    @GetMapping("/{userId}/events/{eventId}")
    public EventDto getById(
            @PathVariable Long userId,
            @PathVariable Long eventId) {

        EventDto eventDto = eventServicePrivate.getById(userId, eventId);
        log.info(" EventControllerPrivate  - getById().  Возвращено {} ", eventDto.toString());
        return eventDto;
    }


    @PatchMapping("/{userId}/events/{eventId}/requests")
    public RequestsListsDto updateRequestStatus(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody(required = false) RequestUpdateStatusDto requestStatusUpdateDto) {

        RequestsListsDto listsDto = eventServicePrivate.updateRequestStatus(requestStatusUpdateDto, userId, eventId);
        log.info("EventControllerPrivate  - updateRequestStatus(). Обновлен статус {} ", listsDto.toString());
        return listsDto;
    }


    @PatchMapping("/{userId}/events/{eventId}")
    public EventDto updateByOwner(
            @RequestBody @Valid EventUpdateByUserDto eventUpdateByUserDto,
            @PathVariable Long userId,
            @PathVariable Long eventId) {

        EventDto eventDto = eventServicePrivate.updateByOwner(eventUpdateByUserDto, userId, eventId);
        log.info(" EventControllerPrivate  - updateByOwner().  Обновлено {} ", eventDto.toString());
        return eventDto;
    }
}

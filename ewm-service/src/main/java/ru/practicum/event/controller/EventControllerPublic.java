package ru.practicum.event.controller;


import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventShortDto;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.service.EventServicePublic;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")

public class EventControllerPublic {

    private final EventServicePublic eventServicePublic;

    @GetMapping("/{id}")
    public EventDto getEventById(@PathVariable Long id, HttpServletRequest request) {
        log.info("EventControllerPublic - getEventById().  " +
                "\n {} " +
                "\n request.getMethod() {}" +
                "\n  request.getRemoteAddr() {}", "HttpServletRequest request", request.getMethod(), request.getRemoteAddr());
        EventDto eventDto = eventServicePublic.getEventById(id, request.getRemoteAddr());
        log.info("EventControllerPublic - getEventById().  Возвращено   {} ", eventDto.toString());
        return eventDto;
    }


    @GetMapping
    public List<EventShortDto> searchEvent(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "EVENT_DATE") String sort,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size,
            HttpServletRequest request) {
        log.info("EventControllerPublic -  searchEvent().  " +
                "\n {} " +
                "\n request.getMethod() {}" +
                "\n  request.getRemoteAddr() {}", "HttpServletRequest request", request.getMethod(), request.getRemoteAddr());

        List<EventShortDto> list = eventServicePublic.searchEvent(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size,
                request.getRemoteAddr());

        log.info("EventControllerPublic - getEventById().  Возвращено список  из  {} элементов ", list.size());

        return list;
    }
}
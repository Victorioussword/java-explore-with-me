package ru.practicum.event.service;

import java.util.List;
import java.util.Comparator;
import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import ru.practicum.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import java.util.stream.Collectors;
import ru.practicum.utils.enums.State;
import ru.practicum.event.model.Event;
import ru.practicum.client.StatClient;
import ru.practicum.event.dto.EventDto;
import java.time.format.DateTimeFormatter;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.Mapper.EventMapper;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exceptions.ObjectNotFoundException;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.category.repository.CategoryRepository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServicePublic {

    private final EventRepository eventRepository;

    private final CategoryRepository categoryRepository;

    private final RequestRepository requestRepository;

    private final StatClient statClient;


    public EventDto getEventById(Long id, String ip) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Event не найден"));

        if (event.getState() != State.PUBLISHED) {
            throw new ObjectNotFoundException("Event не опубликован");
        }
        EventDto eventDto = EventMapper.toEventDto(event);
        Utils.saveHit(ip, id, statClient);
        if (eventDto.getViews() == null) {
            eventDto.setViews(1L);
        } else {
            eventDto.setViews(eventDto.getViews() + 1);
        }
        log.info("EventServicePrivate - getEventById() Возвращено Event  {}", eventDto.toString());
        return eventDto;
    }


//2023-06-25 00:04:14 2023-06-24 20:04:14.993 ERROR 1 --- [nio-8080-exec-6] o.a.c.c.C.[.[.[/].[dispatcherServlet]
// : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed;
// nested exception is java.lang.IllegalArgumentException:
// Cannot deserialize value of type `java.util.ArrayList<ru.practicum.stat.dto.ViewStatDto>` from Object value (token `JsonToken.START_OBJECT`)
//2023-06-25 00:04:14  at [Source: UNKNOWN; byte offset: #UNKNOWN]] with root cause
//2023-06-25 00:04:14
//2023-06-25 00:04:14 com.fasterxml.jackson.databind.exc.MismatchedInputException: Cannot deserialize value of type `java.util.ArrayList<ru.practicum.stat.dto.ViewStatDto>` from Object value (token `JsonToken.START_OBJECT`)
//2023-06-25 00:04:14  at [Source: UNKNOWN; byte offset: #UNKNOWN]

    @Transactional
    public List<EventShortDto> searchEvent(String text,
                                           List<Long> categories,
                                           Boolean paid,
                                           String rangeStart,
                                           String rangeEnd,
                                           Boolean onlyAvailable,
                                           String sort,
                                           int from,
                                           int size,
                                           String ip) {

        LocalDateTime dateStartSearch = LocalDateTime.now().plusSeconds(1L);
        LocalDateTime dateEndSearch = LocalDateTime.now().plusYears(99L);
        if (rangeStart != null) {
            dateStartSearch = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (rangeEnd != null) {
            dateEndSearch = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (categories == null || categories.size() == 0) {
            categories = categoryRepository.findAll().stream()
                    .map(category -> category.getId())
                    .collect(Collectors.toList());
        }

        List<Event> events = eventRepository.searchEventPub(text, categories, paid, dateStartSearch, dateEndSearch, State.PUBLISHED, PageRequest.of(from, size));

        if (onlyAvailable) {
            events = events.stream()
                    .filter(event -> event.getParticipantLimit() > Utils.confirmedRequests(event.getId(), requestRepository))
                    .collect(Collectors.toList());
        }
        log.info("111111111");
        List<EventShortDto> eventShortDtos = events.stream()
                .map(EventMapper::toShortDto)
                .peek(shortDto -> {

                        shortDto.setViews(Utils.getViews(rangeStart,
                                rangeEnd,
                                "/events/" + shortDto.getId(),
                                false,
                                statClient));

                })
                .collect(Collectors.toList());
        log.info("444444444");
        if (sort.equals("VIEWS")) {
            eventShortDtos.stream()
                    .sorted(Comparator.comparing(EventShortDto::getViews));
        }
        log.info("555555555555");
        Utils.saveHit(ip, null, statClient);
        log.info("666666666666");
        if (eventShortDtos.isEmpty()) {
            throw new BadRequestException("Не найдены данные.");
        }
        log.info("EventServicePrivate - searchEvent() Возвращено список из   {} элементов", eventShortDtos.size());
        return eventShortDtos;
    }
}
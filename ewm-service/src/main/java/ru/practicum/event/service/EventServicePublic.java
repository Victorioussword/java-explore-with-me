package ru.practicum.event.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import ru.practicum.utils.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.stream.Collectors;

import ru.practicum.utils.enums.State;
import ru.practicum.event.model.Event;
import ru.practicum.client.StatClient;
import ru.practicum.event.dto.EventDto;

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

    private final StatClient statClient;

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final CategoryRepository categoryRepository;


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


    @Transactional
    public List<EventShortDto> searchEvent(String text,
                                           List<Long> categories,
                                           Boolean paid,
                                           LocalDateTime inputRangeStart,
                                           LocalDateTime inputRangeEnd,
                                           Boolean onlyAvailable,
                                           String sort,
                                           int from,
                                           int size,
                                           String ip) {

        // значения для времени поиска по умолчанию
        LocalDateTime rangeStart = LocalDateTime.now().plusSeconds(1L).withNano(0);
        LocalDateTime rangeEnd = LocalDateTime.now().plusYears(99L).withNano(0);

        if (inputRangeStart != null) {
            rangeStart = inputRangeStart;

        }
        if (inputRangeEnd != null) {
            rangeEnd = inputRangeEnd;
        }

        if (rangeEnd.isBefore(rangeStart) || rangeEnd.isBefore(LocalDateTime.now())) {
            log.info("BAD_REQUEST - End перед Start");
            throw new BadRequestException("END перед Start");
        }
        // список ID категорий
        if (categories == null || categories.size() == 0) {
            categories = categoryRepository.findAll().stream()
                    .map(category -> category.getId())
                    .collect(Collectors.toList());
        }

        //список найденых событий
        List<Event> events = eventRepository
                .searchEventPub(text, categories, paid, rangeStart, rangeEnd, State.PUBLISHED, PageRequest.of(from, size));

        // сортировка по доступности
        if (onlyAvailable) {
            events = events.stream()
                    .filter(event -> event.getParticipantLimit() > Utils.confirmedRequests(event.getId(), requestRepository))
                    .collect(Collectors.toList());
        }

        log.info("Завершен поиск и сортировка Event");

        List<EventShortDto> eventShortDtos2 = events.stream()
                .map(EventMapper::toShortDto).collect(Collectors.toList());  // без простмотров

        List<String> urises = new ArrayList<>();   // список ссылок для отправки
        for (int i = 0; i < eventShortDtos2.size(); i++) {
            urises.add("/events/" + eventShortDtos2.get(i).getId());
        }

        Map<Long, Long> counts = Utils.getViews2(
                rangeStart,
                rangeEnd,
                urises,
                false,
                statClient);

        for (int i = 0; i < eventShortDtos2.size(); i++) {  // установили просмотры
            eventShortDtos2.get(i).setViews(counts.get(eventShortDtos2.get(i).getId()));
        }

        log.info("Event -> EventShortDto");

        if (sort.equals("VIEWS")) {
            eventShortDtos2.stream()
                    .sorted(Comparator.comparing(EventShortDto::getViews));
        }
        log.info("Попытка сохранения Hit");
        Utils.saveHit(ip, null, statClient);
        log.info("Utils.saveHit - Done");

        log.info("EventServicePrivate - searchEvent() Возвращено список из   {} элементов", eventShortDtos2.size());
        return eventShortDtos2;
    }
}
package ru.practicum.event.service;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.time.LocalDateTime;

import ru.practicum.event.dto.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

import ru.practicum.user.model.User;
import ru.practicum.utils.enums.State;
import ru.practicum.event.model.Event;
import ru.practicum.client.StatClient;
import ru.practicum.request.model.Status;
import ru.practicum.utils.enums.StateAction;
import ru.practicum.category.model.Category;
import ru.practicum.event.Mapper.EventMapper;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.CountRequests;

import static java.util.stream.Collectors.toMap;

import ru.practicum.exceptions.ConflictException;
import ru.practicum.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exceptions.ObjectNotFoundException;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.category.repository.CategoryRepository;


@Slf4j
@Service
@AllArgsConstructor
public class EventServiceAdmin {

    private final StatClient statClient;

    StatisticService statisticService;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final CategoryRepository categoryRepository;


    public List<EventDto> getEventsByAdmin(List<Long> users,
                                           List<String> states,
                                           List<Long> categories,
                                           LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd,
                                           int from,
                                           int size) {

        List<State> stateArrayList = new ArrayList<>();

        if (users == null || users.size() == 0) {
            users = userRepository.findAll().stream().map(User::getId).collect(Collectors.toList());
        }

        if (states != null) {
            for (String state : states) {
                stateArrayList.add(State.valueOf(state));
            }
        } else {
            stateArrayList = Arrays.asList(State.values());
        }

        if (categories == null || categories.size() == 0) {
            categories = categoryRepository.findAll().stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
        }

        LocalDateTime dateStartSearch = LocalDateTime.now().plusYears(999L);
        LocalDateTime dateEndSearch = LocalDateTime.now().minusYears(50L);

        if (rangeStart != null) {
            dateStartSearch = rangeStart;
        }
        if (rangeEnd != null) {
            dateEndSearch = rangeEnd;
        }

        List<Event> events = eventRepository
                .findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateIsAfterAndEventDateIsBefore(users,
                        stateArrayList,
                        categories,
                        dateStartSearch,
                        dateEndSearch,
                        PageRequest.of(from, size));


        if (events.isEmpty()) {
            events.addAll(eventRepository.findAllBy(PageRequest.of(from, size)));
        }
        log.info("EventServicePrivate -  getEventsByAdmin() найден список из {} элементов ", events.size());

        List<EventDto> eventDtos = events.stream()
                .map(EventMapper::toEventDto)
                .collect(Collectors.toList());

        List<String> urises = new ArrayList<>();
        for (int i = 0; i < eventDtos.size(); i++) {
            urises.add("/events/" + eventDtos.get(i).getId());
        }

        Map<Long, Long> counts = statisticService.getViews2(
                rangeStart,
                rangeEnd,
                urises,
                false,
                statClient);
        log.info("EventServicePrivate -  getEventsByAdmin() ПОдготовлена информация о запросах - {} ", counts.size());

        for (int i = 0; i < eventDtos.size(); i++) {
            if (counts.size() == 0)
                eventDtos.get(i).setViews(0L);
            else {
                eventDtos.get(i).setViews(counts.get(eventDtos.get(i).getId()));
            }
        }
        Map<Long, Long> eventToCount = requestRepository.getCountOfRequests(events, Status.CONFIRMED).stream()
                .collect(toMap(CountRequests::getEventId, CountRequests::getCountOfRequests));

        for (int i = 0; i < eventDtos.size(); i++) {
            if (eventToCount.get(eventDtos.get(i).getId()) == null) {
                eventDtos.get(i).setConfirmedRequests(0L);
            } else {
                eventDtos.get(i).setConfirmedRequests(eventToCount.get(eventDtos.get(i).getId()));
            }
        }
        return eventDtos;
    }


    public EventDto updateEvent(EventUpdateByAdminDto eventUpdateByAdminDto, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event не найден"));

        if (eventUpdateByAdminDto.getEventDate() != null) {
            if (eventUpdateByAdminDto.getEventDate().isBefore(LocalDateTime.now().plusHours(1L))) {
                throw new BadRequestException("Время начала должно быть в будущем.");
            }
        }
        if (!event.getState().equals(State.PENDING)) {
            throw new ConflictException("Обновление State не доступно.");
        }
        event = prepareEventAdm(eventUpdateByAdminDto, event);

        EventDto eventDto = EventMapper.toEventDto(event);

        Map<Long, Long> vies = statisticService.getViews2(null, null, List.of("/events/" + eventId), false, statClient);

        eventDto.setViews(vies.get(eventDto.getId()));
        log.info("EventServicePrivate -  updateEvent(). Обновлено {}", eventDto.toString());
        return eventDto;
    }


    private Event prepareEventAdm(EventUpdateByAdminDto eventUpdateByAdminDto, Event event) {

        log.info("Utils.prepareEvent - Начало метода: \n {} \n {} ", eventUpdateByAdminDto, event);

        if (eventUpdateByAdminDto.getAnnotation() != null && !eventUpdateByAdminDto.getAnnotation().isBlank()) {
            event.setAnnotation(eventUpdateByAdminDto.getAnnotation());
        }

        if (eventUpdateByAdminDto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(eventUpdateByAdminDto.getCategory()).orElseThrow(() ->
                    new ObjectNotFoundException("Категория с таким id не существует")));
        }

        if (eventUpdateByAdminDto.getDescription() != null && !eventUpdateByAdminDto.getDescription().isBlank()) {
            event.setDescription(eventUpdateByAdminDto.getDescription());
        }

        if (eventUpdateByAdminDto.getEventDate() != null) {
            event.setEventDate(eventUpdateByAdminDto.getEventDate());
        }

        if (eventUpdateByAdminDto.getLocation() != null) {
            event.setLocation(eventUpdateByAdminDto.getLocation());
        }

        if (eventUpdateByAdminDto.getPaid() != null) {
            event.setPaid(eventUpdateByAdminDto.getPaid());
        }

        if (eventUpdateByAdminDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpdateByAdminDto.getParticipantLimit());
        }

        if (eventUpdateByAdminDto.getRequestModeration() != null) {
            event.setRequestModeration(eventUpdateByAdminDto.getRequestModeration());
        }

        if (eventUpdateByAdminDto.getTitle() != null && !eventUpdateByAdminDto.getTitle().isBlank()) {
            event.setTitle(eventUpdateByAdminDto.getTitle());
        }

        if (eventUpdateByAdminDto.getStateAction() != null
                && eventUpdateByAdminDto.getStateAction().equals(StateAction.PUBLISH_EVENT)
                && event.getState().equals(State.PENDING)) {
            event.setState(State.PUBLISHED);
        }

        if (eventUpdateByAdminDto.getStateAction() != null && eventUpdateByAdminDto.getStateAction()
                .equals(StateAction.REJECT_EVENT) && !event.getState().equals(State.PUBLISHED)) {
            event.setState(State.CANCELED);
        }

        event = eventRepository.save(event);
        log.info("Utils.prepareEventAdm() - Возвращено: {}", event);
        return event;
    }
}
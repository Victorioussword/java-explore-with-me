package ru.practicum.event.service;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.time.LocalDateTime;

import ru.practicum.event.dto.*;
import ru.practicum.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.stream.Collectors;
import ru.practicum.user.model.User;
import ru.practicum.utils.enums.State;
import ru.practicum.event.model.Event;
import ru.practicum.client.StatClient;
import ru.practicum.request.model.Status;
import ru.practicum.category.model.Category;
import ru.practicum.event.Mapper.EventMapper;
import org.springframework.stereotype.Service;
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
        log.info("EventServicePrivate -  getEventsByAdmin() Возвращен список {}", events.size());

        return events.stream()
                .map(EventMapper::toEventDto)
                .peek(eventDto -> eventDto.setConfirmedRequests(requestRepository
                        .countByEventIdAndStatus(eventDto.getId(), Status.CONFIRMED)))
                .peek(eventDto -> {

                    eventDto
                            .setViews(Utils.getViews(rangeStart, rangeEnd, "/events/" + eventDto.getId(), false, statClient));

                })
                .collect(Collectors.toList());
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
        event = Utils.prepareEventAdm(eventUpdateByAdminDto, event, categoryRepository, eventRepository);

        EventDto eventDto = EventMapper.toEventDto(event);

        eventDto.setViews(Utils.getViews(null, null, "/events/" + eventId, false, statClient));
        log.info("EventServicePrivate -  updateEvent(). Обновлено {}", eventDto.toString());
        return eventDto;
    }
}
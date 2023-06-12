package ru.practicum.ewmservice.event.service;
// Спецификация:
// https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-main-service-spec.json

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.category.repository.CategoryRepository;
import ru.practicum.ewmservice.event.dto.ForEventContollerAdm.UpdateEventByAdminDto;
import ru.practicum.ewmservice.event.dto.ForEventContollerUser.InputEventDto;
import ru.practicum.ewmservice.event.dto.ForEventContollerUser.OutputEventShortDto;
import ru.practicum.ewmservice.event.dto.ForEventContollerUser.OutputFullEventDto;
import ru.practicum.ewmservice.event.dto.ForEventContollerUser.UpdateEventByUserDto;
import ru.practicum.ewmservice.event.mapper.EventMapper;
import ru.practicum.ewmservice.event.model.*;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.event.repository.LocationRepository;
import ru.practicum.ewmservice.exception.BadRequestException;
import ru.practicum.ewmservice.exception.CreateEventException;
import ru.practicum.ewmservice.exception.NotFoundException;
import ru.practicum.ewmservice.exception.ValidationException;
import ru.practicum.ewmservice.request.RequestMapper;
import ru.practicum.ewmservice.request.dto.OutputRequestDto;
import ru.practicum.ewmservice.request.model.Request;
import ru.practicum.ewmservice.request.model.RequestStatus;
import ru.practicum.ewmservice.request.repository.RequestRepository;

import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;
import ru.practicum.stat.client.StatClient;
import ru.practicum.stat.dto.InputHitDto;
import ru.practicum.stat.dto.ViewStat;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;

    private final StatClient statClient;

    public static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int TIME_FOR_PREPARE = 2;
    private Sort sort = Sort.by(Sort.Direction.DESC, "id");

    //Private/
    public OutputFullEventDto createEvent(InputEventDto inputEventDto, Long userId) {

        log.info("EventService - createEvent() Пользователь № {}. Событие {}", userId, inputEventDto.toString());

        checkTime(inputEventDto.getEventDate());

        User initiator = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User - создатель мероприятия не найден не найден");

        });
        Category category = categoryRepository.findById(inputEventDto.getCategory()).orElseThrow(() -> {
            throw new NotFoundException("Категория не найдена");
        });

        Event event = EventMapper.toEvent(inputEventDto, category, initiator);
        return EventMapper.toOutputFullEventDto(eventRepository.save(event));
    }

    private void checkTime(LocalDateTime timeOfEvent) {
        if (timeOfEvent == null || LocalDateTime.now().isAfter(timeOfEvent.minusHours(TIME_FOR_PREPARE))) {
            throw new CreateEventException(String.format("Field: eventDate. Error: остается слишком мало времени для " +
                    "подготовки. Value: %s", timeOfEvent));
        }
    }


    // private
    public OutputFullEventDto getEventByIdAndUser(long userId, long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User - создатель мероприятия не найден");
        });
        Event event = eventRepository.findEventsById(eventId);
        OutputFullEventDto dto = addConfRecToFull(event);
        return dto;
    }


    public List<OutputEventShortDto> search(String text,
                                            List<Long> categoryIds,
                                            Boolean paid,
                                            String start,
                                            String end,
                                            boolean available,
                                            EventSort sort,
                                            int from,
                                            int size,
                                            HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        LocalDateTime startSearch = LocalDateTime.now().plusSeconds(1L);
        LocalDateTime endSearch = LocalDateTime.now().plusYears(999L);

        if (start != null) {
            startSearch = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (end != null) {
            endSearch = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (categoryIds == null || categoryIds.size() == 0) {
            categoryIds = categoryRepository.findAll().stream().map(cat -> cat.getId()).collect(Collectors.toList());
        }
        Pageable pageable = PageRequest.of(from, size);

        List<Event> events = eventRepository.search(text, categoryIds, paid, startSearch, endSearch, State.PUBLISHED, pageable);


        if (available) {
            events = events
                    .stream()
                    .filter(event -> event.getParticipantLimit() > countConfRequests2(event)).collect(Collectors.toList());
        }

        List<OutputEventShortDto> eventShorts = events.stream()
                .map(EventMapper::toOutputEventShortDto)
                .peek(e -> e.setViews(showViews(
                        LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        "/events/" + e.getId(),
                        false)))
                .collect(Collectors.toList());
        if (sort.equals(EventSort.VIEWS)) {
            eventShorts.stream()
                    .sorted(Comparator.comparing(OutputEventShortDto::getViews));
        }
        saveHit(ip, null);
        if (eventShorts.isEmpty()) {
            throw new BadRequestException("По данным параметрам ничего не найдено");
        }
        return eventShorts;
    }

    private Long countConfRequests2(Event event) {


        return requestRepository.countByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED);
    }


    public OutputFullEventDto getEventByPublic(Long id, HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event не найден"));

        if (event.getState() != State.PUBLISHED) {
            throw new NotFoundException("Event не опубликован");
        }
        OutputFullEventDto dto = EventMapper.toOutputFullEventDto(event);
        saveHit(ip, id);
        if (dto.getViews() == null) {
            dto.setViews(1L);
        } else {
            dto.setViews(dto.getViews() + 1);
        }
        return dto;
    }


    private Long getViews2(String rangeStart, String rangeEnd, String uris, Boolean unique) {
        List<ViewStat> dto = statClient.getStat(
                LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                List.of(uris),

                unique);
        return dto.size() > 0 ? dto.get(0).getHits() : 0L;
    }

    // private
    public OutputFullEventDto updateEvent(long userId, long eventId, UpdateEventByUserDto updateEventByUserDto) {

        Event event = eventRepository.findById(eventId).orElseThrow();

        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Пользователь не может обновить событие");
        }
        if (updateEventByUserDto.getEventDate() != null) {
            LocalDateTime time = LocalDateTime.parse(updateEventByUserDto.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (LocalDateTime.now().isAfter(time.minusHours(2))) {
                throw new ValidationException("Должно оставаться 2 часа до события");
            }
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ValidationException("Событие уже было опубликовано");
        }
        if (updateEventByUserDto.getCategory() != null && !Objects.equals(updateEventByUserDto.getCategory(),
                event.getCategory().getId())) {
            Category category = categoryRepository.findById(updateEventByUserDto.getCategory()).orElseThrow();
            event.setCategory(category);
        }
        if (updateEventByUserDto.getLocation() != null) {
            Location location = locationRepository.save(updateEventByUserDto.getLocation());
            event.setLocation(location);
        }
        toEventFromUpdateRequestDto(event, updateEventByUserDto);
        OutputFullEventDto dto = EventMapper.toOutputFullEventDto(event);
        dto.setConfirmedRequests((long) requestRepository.findAllByEventAndStatus(event, RequestStatus.CONFIRMED)
                .size());


        Long viewsQuantity = showViews(
                LocalDateTime.parse("1971-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse("2999-12-31 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "/events/" + eventId,
                false);
        dto.setViews(viewsQuantity);
        return dto;
    }


    public List<OutputFullEventDto> getEventsByAdmin2(List<Long> users, List<String> states, List<Long> categories, String rangeStart,
                                                      String rangeEnd, int from, int size) {
        List<State> statesEnum = new ArrayList<>();
        if (states != null) {
            for (String state : states) {
                statesEnum.add(State.valueOf(state));
            }
        } else {
            statesEnum = Arrays.asList(State.values());
        }
        LocalDateTime dateStartSearch = LocalDateTime.now().plusYears(99L);
        LocalDateTime dateEndSearch = LocalDateTime.now().minusYears(99L);
        if (rangeStart != null) {
            dateStartSearch = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (rangeEnd != null) {
            dateEndSearch = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (users == null || users.size() == 0) {
            users = userRepository.findAll().stream()
                    .map(User::getId)
                    .collect(Collectors.toList());
        }
        if (categories == null || categories.size() == 0) {
            categories = categoryRepository.findAll().stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
        }
        Pageable pageable = PageRequest.of(from, size);
        List<Event> events = eventRepository
                .findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateIsAfterAndEventDateIsBefore(
                        users,
                        statesEnum,
                        categories,
                        dateStartSearch,
                        dateEndSearch,
                        pageable);
        if (events.isEmpty()) {
            events.addAll(eventRepository.findAllBy(PageRequest.of(from, size)));
        }
        return events.stream()
                .map(EventMapper::toOutputFullEventDto)
                .peek(e -> e.setConfirmedRequests(requestRepository.countByEventIdAndStatus(e.getId(), RequestStatus.CONFIRMED)))
                .peek(e -> e.setViews(getViews2(rangeStart, rangeEnd, "/events/" + e.getId(), false)))
                .collect(Collectors.toList());
    }

    public List<OutputEventShortDto> getEventsByUser(long userId, int from, int size) {
        PageRequest pageable = PageRequest.of(from / size, size);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User - создатель мероприятия не найден");
        });
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);

        List<OutputEventShortDto> outputEventShortDtos = toDtoAndSetConfirmedRequests(events);
        return events.stream()
                .map(EventMapper::toOutputEventShortDto)
                .peek(e -> e.setConfirmedRequests(requestRepository.countByEventIdAndStatus(e.getId(), RequestStatus.CONFIRMED)))
                .peek(e -> e.setViews(getViews2(
                        "1971-01-01 00:00:00",
                        "2999-12-31 00:00:00",
                        "/events/" + e.getId(), false)))
                .collect(Collectors.toList());
    }

    ///////////////////////////////////////////////////////////////ГОТОВО///////////////////////////////////////


    public OutputFullEventDto updateEventByAdmin(Long eventId, UpdateEventByAdminDto updateEventByAdminDto) {

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event для обновления не найден"));
        if (updateEventByAdminDto.getEventDate() != null) {
            if (updateEventByAdminDto.getEventDate().isBefore(LocalDateTime.now().plusHours(1L))) {
                throw new BadRequestException("EventDate в прошлом");
            }
        }
        if (event.getState().equals(State.PUBLISHED) || event.getState().equals(State.CANCELED)) {
            throw new ValidationException("Обновление не доступно");
        }

        if (updateEventByAdminDto.getAnnotation() != null) {
            event.setAnnotation(updateEventByAdminDto.getAnnotation());
        }
        if (updateEventByAdminDto.getCategory() != null) {

            Optional<Category> category = categoryRepository.findById(updateEventByAdminDto.getCategory());
            event.setCategory(category.get());
        }
        if (updateEventByAdminDto.getDescription() != null) {
            event.setDescription(updateEventByAdminDto.getDescription());
        }
        if (updateEventByAdminDto.getEventDate() != null) {
            event.setEventDate(updateEventByAdminDto.getEventDate());
        }
        if (updateEventByAdminDto.getLocation() != null) {
            event.setLocation(updateEventByAdminDto.getLocation());
        }
        if (updateEventByAdminDto.getPaid() != null) {
            event.setPaid(updateEventByAdminDto.getPaid());
        }
        if (updateEventByAdminDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventByAdminDto.getParticipantLimit());
        }
        if (updateEventByAdminDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventByAdminDto.getRequestModeration());
        }
        if (updateEventByAdminDto.getTitle() != null) {
            event.setTitle(updateEventByAdminDto.getTitle());
        }
        if (updateEventByAdminDto.getStateAction() != null
                && updateEventByAdminDto.getStateAction().equals(UserStateAction.PUBLISH_EVENT) && event.getState().equals(State.PENDING)) {
            event.setState(State.PUBLISHED);
        }
        if (updateEventByAdminDto.getStateAction() != null
                && updateEventByAdminDto.getStateAction().equals(UserStateAction.REJECT_EVENT) && !event.getState().equals(State.PUBLISHED)) {
            event.setState(State.CANCELED);
        }
        event = eventRepository.save(event);


        OutputFullEventDto dto = EventMapper.toOutputFullEventDto(event);

        dto.setViews(showViews(null, null, "/events/" + eventId, false));


        return dto;
    }


    private long showViews(LocalDateTime start,  /** ГОТОВО */
                           LocalDateTime end,
                           String uris,
                           Boolean unique) {

        List<ViewStat> dto = statClient.getStat(
                start,
                end,
                List.of(uris),
                unique);
        return dto.size() > 0 ? dto.get(0).getHits() : 0L;
    }

    private OutputFullEventDto addConfRecToFull(Event event) {  /** ГОТОВО */

        List<OutputRequestDto> requests = requestRepository.findAllByEventAndStatus(event, RequestStatus.CONFIRMED)
                .stream().map(RequestMapper::toOutputRequestDto).collect(Collectors.toList());
        OutputFullEventDto dto = EventMapper.toOutputFullEventDto(event);
        dto.setConfirmedRequests((long) requests.size());
        return dto;
    }


    private List<OutputEventShortDto> toDtoAndSetConfirmedRequests(List<Event> events) {  /** ГОТОВО */
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<Long> idsOfEvents = events.stream().map(Event::getId).collect(Collectors.toList());
        List<OutputEventShortDto> dtos = events.stream().map(EventMapper::toOutputEventShortDto).collect(Collectors.toList());

        List<Request> requests = requestRepository.findAllConfirmedByEventIdIn(idsOfEvents, sort);

        Map<Event, List<Request>> requestListMap = requestRepository.findAllConfirmedByEventIdIn(idsOfEvents, sort)
                .stream().collect(groupingBy(Request::getEvent, toList()));
        List<OutputEventShortDto> dtos2 = new ArrayList<>();

        for (int i = 0; i < events.size(); i++) {
            if (!requestListMap.isEmpty() && requestListMap.containsKey(events.get(i))) {
                OutputEventShortDto dto = EventMapper.toOutputEventShortDto(events.get(i));
                long confirmed = requestListMap.get(events.get(i)).size();
                dto.setConfirmedRequests(confirmed);
                dtos2.add(dto);

            }
        }
        return dtos2;
    }

    public static void toEventFromUpdateRequestDto(Event event,  /** ГОТОВО */
                                                   UpdateEventByUserDto dto) {
        if (Objects.equals(dto.getStateAction(), UserStateAction.CANCEL_REVIEW.name())) {
            event.setState(State.CANCELED);
        }
        if (Objects.equals(dto.getStateAction(), UserStateAction.SEND_TO_REVIEW.name())) {
            event.setState(State.PENDING);
        }
        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getDescription() != null) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(dto.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
        if (dto.getParticipantLimit() != null) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }
        if (dto.getTitle() != null) {
            event.setTitle(dto.getTitle());
        }
    }


    private void saveHit(String ip, Long eventId) {   /** ГОТОВО */
        InputHitDto hitDto = new InputHitDto();
        hitDto.setApp("ewm-service");
        hitDto.setTimestamp(LocalDateTime.now());
        hitDto.setIp(ip);
        if (eventId == null) {
            hitDto.setUri("/events");
        } else {
            hitDto.setUri("/events/" + eventId);
        }
        statClient.postHit(hitDto);
    }


}
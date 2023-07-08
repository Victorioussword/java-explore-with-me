package ru.practicum.event.service;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import java.util.stream.Collectors;
import ru.practicum.user.model.User;
import ru.practicum.utils.enums.State;
import ru.practicum.event.model.Event;
import lombok.RequiredArgsConstructor;
import ru.practicum.event.dto.EventDto;
import ru.practicum.request.model.Status;
import ru.practicum.request.model.Request;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.utils.enums.StateAction;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.EventInputDto;
import ru.practicum.event.Mapper.EventMapper;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.request.dto.RequestsListsDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import ru.practicum.event.dto.EventUpdateByUserDto;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.request.dto.RequestUpdateStatusDto;
import ru.practicum.exceptions.ObjectNotFoundException;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.category.repository.CategoryRepository;
import org.springframework.transaction.annotation.Transactional;



@Slf4j
@Service
@RequiredArgsConstructor
public class EventServicePrivate {

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final CategoryRepository categoryRepository;


    public EventDto create(EventInputDto eventInputDto, Long userId) {

        checkEventDateDeforeCreate(eventInputDto);

        Category category = categoryRepository.findById(eventInputDto.getCategory()).orElseThrow(() ->
                new ObjectNotFoundException("категория с таким id не существует"));

        User user = userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("USer не найден"));

        Event event = EventMapper.toEvent(eventInputDto, category, user);

        event.setPaid(eventInputDto.isPaid());

        if (eventInputDto.getParticipantLimit() == null) {
            event.setParticipantLimit(0L);
        }

        if (eventInputDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        event = eventRepository.save(event);
        EventDto eventDto = EventMapper.toEventDto(event);
        UserShortDto userShortDto = UserMapper.toUserShortDto(user);
        eventDto.setInitiator(userShortDto);
        log.info("EventServicePrivate - create() Создан Event  {}", eventDto.toString());
        return eventDto;
    }

    public List<EventShortDto> getEventsByUser(Long userId, int from, int size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("USer не найден"));

        List<Event> events = eventRepository.findAllByInitiatorId(user.getId(), PageRequest.of(from, size));

        log.info("EventServicePrivate - getEventsByUser() Возвращен список  {}", events.size());

        return events.stream()
                .map(EventMapper::toShortDto)
                .peek(shortDto -> shortDto.setConfirmedRequests(requestRepository.countByEventIdAndStatus(shortDto.getId(), Status.CONFIRMED)))
                .collect(Collectors.toList());
    }

    public EventDto getById(Long userId, Long eventId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("USer не найден"));

        eventRepository.findById(eventId).orElseThrow(() -> new ObjectNotFoundException("Event не найден"));

        Event event = eventRepository.findByInitiatorIdAndId(user.getId(), eventId);
        log.info("EventServicePrivate - getEventsByUser() Возвращен Event {}", event.toString());
        return EventMapper.toEventDto(event);
    }


    public EventDto updateByOwner(EventUpdateByUserDto dto, Long userId, Long eventId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("USer не найден"));

        eventRepository.findById(eventId).orElseThrow(() -> new ObjectNotFoundException("Event не найден"));

        Event event = eventRepository.findByInitiatorIdAndId(user.getId(), eventId);
        if (dto.getEventDate() != null) {
            if (dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2L)) && dto.getEventDate() != null) {
                throw new BadRequestException("Не возможно изменить время. Перед событием должно быть 2 часа.");
            }
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Обновление события не возможно.");
        }
        event = prepareEventforPrivate(dto, event);
        EventDto eventDto = EventMapper.toEventDto(event);

        log.info("EventServicePrivate - getRequestsForEvent() Возвращено {}", eventDto.toString());
        return eventDto;
    }


    @Transactional(readOnly = true)
    public List<RequestDto> getRequestsForEvent(Long userId, Long eventId) {
        log.info("EventServicePrivate. Начало метода getRequestsForEvent()");
        if (!userRepository.existsById(userId)) {
            log.info("Проверка наличия User в б.д.");
            throw new ObjectNotFoundException("USer не найден");
        }
        if (!eventRepository.existsById(eventId)) {
            log.info("Проверка наличия Event в б.д.");
            throw new ObjectNotFoundException("Event не найден");
        }
        List<Request> requests = requestRepository.findAllByEventId(eventId);
        log.info("EventServicePrivate - getRequestsForEvent() Возвращен список из {}", requests.size());

        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public RequestsListsDto updateRequestStatus(RequestUpdateStatusDto requestStatusUpdateDto, Long userId, Long eventId) {

        log.info("EventServicePrivate -updateRequestStatus(). Начало метода");

        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("USer не найден");
        }

        if (requestStatusUpdateDto == null) {
            throw new ConflictException("Новый статус не был задан");
        }

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ObjectNotFoundException("Event не найден"));

        List<Request> requests = requestRepository.findByIdIn(requestStatusUpdateDto.getRequestIds());

        if (requestRepository.countByEventId(event.getId()) > event.getParticipantLimit()) {   // TODO изменил знак сравнения было >=
            throw new ConflictException("Достигнут лимит запросов на участие");
        }
        List<RequestDto> confRequestsDtos = new ArrayList<>();
        List<RequestDto> rejRequestsDtos = new ArrayList<>();

        if (Status.CONFIRMED.equals(requestStatusUpdateDto.getStatus()) && requests.size() > 0) {

            int canceled = 0;

            int maxPart = maxPartCount(event.getParticipantLimit(), requests.size());

            for (int i = canceled; i < requests.size(); i++) {
                isStatusConfirmed(requests.get(i));
                requests.get(i).setStatus(Status.REJECTED);
                rejRequestsDtos.add(RequestMapper.toRequestDto(requests.get(i)));
            }

            for (int i = 0; i < maxPart; i++) {
                requests.get(i).setStatus(Status.CONFIRMED);
                canceled++;
                confRequestsDtos.add(RequestMapper.toRequestDto(requests.get(i)));
            }

        } else {
            for (int i = 0; i < requests.size(); i++) {
                isStatusConfirmed(requests.get(i));
                requests.get(i).setStatus(Status.REJECTED);
                rejRequestsDtos.add(RequestMapper.toRequestDto(requests.get(i)));
            }
        }
        log.info("EventServicePrivate - updateRequestStatus(). Возвращена сущность RequestsListsDto: Confirmed {}, Rejected {}",
                confRequestsDtos.size(),
                rejRequestsDtos.size());

        return new RequestsListsDto(confRequestsDtos, rejRequestsDtos);
    }

    public Event prepareEventforPrivate(EventUpdateByUserDto dto,
                                        Event event) {
        if (dto.getAnnotation() != null && !dto.getAnnotation().isBlank()) {    // todo добавил isBlank.
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getCategory() != null) {

            Category category = categoryRepository.findById(dto.getCategory()).orElseThrow(() ->
                    new ObjectNotFoundException("категория с таким id не существует"));
            event.setCategory(category);
        }
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {  // todo добавил isBlank.
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getLocation() != null) {
            event.setLocation(dto.getLocation());
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
        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {   // todo добавил isBlank.
            event.setTitle(dto.getTitle());
        }
        if (dto.getStateAction() != null && dto.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
            event.setState(State.PENDING);
        }
        if (dto.getStateAction() != null && dto.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
            event.setState(State.CANCELED);
        }
        event = eventRepository.save(event);
        log.info("Utils. prepareEventforPrivate() - Возвращено: {}", event);
        return event;
    }


    private int maxPartCount(Long a, int b) {
        int max;
        if (a <= b) max = a.intValue();
        else max = b;
        return max;
    }


    private void checkEventDateDeforeCreate(EventInputDto eventInputDto) {

        if (eventInputDto.getEventDate().isBefore(LocalDateTime.now()) ||
                eventInputDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2L))) {
            throw new ConflictException("EventDate должно быть в будущем. До события должно быть не менее 2 часов");
        }
    }

    private void isStatusConfirmed(Request request) {
        if (request.getStatus().equals(Status.CONFIRMED)) {
            throw new ConflictException("Status не возможно изменить.");
        }
    }

}
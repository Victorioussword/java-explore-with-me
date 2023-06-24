package ru.practicum.utils;

import java.util.List;
import java.time.LocalDateTime;

import ru.practicum.client.StatClient;
import ru.practicum.event.model.Event;
import ru.practicum.stat.dto.HitDto;
import ru.practicum.utils.enums.State;
import ru.practicum.stat.dto.ViewStatDto;
import ru.practicum.request.model.Status;
import ru.practicum.request.model.Request;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.EventInputDto;
import ru.practicum.utils.enums.StateAction;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.event.dto.EventUpdateByUserDto;
import ru.practicum.event.dto.EventUpdateByAdminDto;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exceptions.ObjectNotFoundException;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.category.repository.CategoryRepository;


public class Utils {

    public static Long getViews(String rangeStart,
                                String rangeEnd,
                                String uris,
                                Boolean unique,
                                StatClient statClient) {

        List<ViewStatDto> dto = statClient.getStat(rangeStart, rangeEnd, List.of(uris), unique);
        if (dto.size() > 0) {
            return dto.get(0).getHits();
        } else {
            return 0L;
        }
    }


    public static void checkEventDateDeforeCreate(EventInputDto eventInputDto) {

        if (eventInputDto.getEventDate().isBefore(LocalDateTime.now()) ||
                eventInputDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2L))) {
            throw new ConflictException("EventDate должно быть в будущем. До события должно быть не менее 2 часов");
        }
    }

    public static Event prepareEvent(EventUpdateByAdminDto eventUpdateByAdminDto,
                                     Event event,
                                     CategoryRepository categoryRepository,
                                     EventRepository eventRepository) {
        if (eventUpdateByAdminDto.getAnnotation() != null) {
            event.setAnnotation(eventUpdateByAdminDto.getAnnotation());
        }

        if (eventUpdateByAdminDto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(eventUpdateByAdminDto.getCategory()).orElseThrow(() ->
                    new ObjectNotFoundException("Категория с таким id не существует")));
        }

        if (eventUpdateByAdminDto.getDescription() != null) {
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

        if (eventUpdateByAdminDto.getTitle() != null) {
            event.setTitle(eventUpdateByAdminDto.getTitle());
        }

        if (eventUpdateByAdminDto.getStateAction() != null && eventUpdateByAdminDto.getStateAction()
                .equals(StateAction.PUBLISH_EVENT) && event.getState().equals(State.PENDING)) {
            event.setState(State.PUBLISHED);
        }

        if (eventUpdateByAdminDto.getStateAction() != null && eventUpdateByAdminDto.getStateAction()
                .equals(StateAction.REJECT_EVENT) && !event.getState().equals(State.PUBLISHED)) {
            event.setState(State.CANCELED);
        }

        event = eventRepository.save(event);
        return event;
    }


    public static Event prepareEvent(EventUpdateByUserDto dto,
                                     Event event,
                                     CategoryRepository categoryRepository,
                                     EventRepository eventRepository) {
        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getCategory() != null) {

            Category category = categoryRepository.findById(dto.getCategory()).orElseThrow(() ->
                    new ObjectNotFoundException("категория с таким id не существует"));
            event.setCategory(category);
        }
        if (dto.getDescription() != null) {
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
        if (dto.getTitle() != null) {
            event.setTitle(dto.getTitle());
        }
        if (dto.getStateAction() != null && dto.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
            event.setState(State.PENDING);
        }
        if (dto.getStateAction() != null && dto.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
            event.setState(State.CANCELED);
        }
        event = eventRepository.save(event);
        return event;
    }


    public static void isStatusConfirmed(Request request) {
        if (request.getStatus().equals(Status.CONFIRMED)) {
            throw new ConflictException("Status не возможно изменить.");
        }
    }


    public static void checkRequest(Event event, Long userId, RequestRepository requestRepository) {
        if (event.getInitiator().getId() == userId) {
            throw new ConflictException("User не может создать на событие. User - создатель события");
        }
        if (event.getState().equals(State.PENDING) || event.getState().equals(State.CANCELED)) {
            throw new ConflictException("Event не опубликовано");
        }
        if (event.getParticipantLimit() != 0) {
            Long countRequest = requestRepository.countByEventId(event.getId());
            if (event.getParticipantLimit() <= countRequest) {
                throw new ConflictException("Превышено количество запросов");
            }
        }
    }

    public static int maxPartCount(Long a, int b) {
        int max;
        if (a <= b) max = a.intValue();
        else max = b;
        return max;
    }

    public static Long confirmedRequests(Long eventId, RequestRepository requestRepository) {
        return requestRepository.countByEventIdAndStatus(eventId, Status.CONFIRMED);
    }

    public static Long viewsEvent(String rangeStart, String rangeEnd, String uris, Boolean unique, StatClient statClient) {
        List<ViewStatDto> viewStatDtos = statClient.getStat(rangeStart, rangeEnd, List.of(uris), unique);

        return viewStatDtos.size() > 0 ? viewStatDtos.get(0).getHits() : 0L;
    }

    public static void saveHit(String ip, Long eventId, StatClient statClient) {
        HitDto hitDto = new HitDto();
        hitDto.setApp("ewm-service");
        hitDto.setTimeStamp(LocalDateTime.now());
        hitDto.setIp(ip);
        if (eventId == null) {
            hitDto.setUri("/events");
        } else {
            hitDto.setUri("/events/" + eventId);
        }
        statClient.saveStat(hitDto);
    }
}
package ru.practicum.request.service;


import java.util.List;
import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;
import java.util.stream.Collectors;
import ru.practicum.user.model.User;
import lombok.RequiredArgsConstructor;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.Status;
import ru.practicum.request.model.Request;
import ru.practicum.request.dto.RequestDto;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exceptions.ObjectNotFoundException;
import ru.practicum.request.repository.RequestRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.utils.enums.State;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;


    @Transactional
    public RequestDto createRequest(Long userId, Long eventId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("User не найден"));

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ObjectNotFoundException("Event не найден"));

        checkRequest(event, userId);

        List<Request> checkExistingRequest = requestRepository.findAllByRequesterIdAndEventId(userId, eventId);

        if (!checkExistingRequest.isEmpty()) {
            throw new ConflictException("Request уже существует");
        }

        Request request;
        if (event.getRequestModeration()) {
            request = new Request(eventId, event, user, LocalDateTime.now(), Status.PENDING);
        } else {
            request = new Request(eventId, event, user, LocalDateTime.now(), Status.CONFIRMED);
        }
        if (event.getParticipantLimit() == 0) {
            request.setStatus(Status.CONFIRMED);
        }
        request = requestRepository.save(request);

        RequestDto dto = RequestMapper.toRequestDto(request);

        return dto;
    }


    @Transactional(readOnly = true)
    public List<RequestDto> getRequests(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("User не найден"));

        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public RequestDto update(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("User не найден"));
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new ObjectNotFoundException("Request не найден"));
        request.setStatus(Status.CANCELED);

        return RequestMapper.toRequestDto(request);
    }


    private void checkRequest(Event event, Long userId) {
        log.info("Utils.checkRequest. НАчало проверки");
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
        log.info("Utils.checkRequest(). Проверка пройдена.");
    }

}
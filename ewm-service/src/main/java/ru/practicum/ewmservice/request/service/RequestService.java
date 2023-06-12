package ru.practicum.ewmservice.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.State;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.exception.CreateEventException;
import ru.practicum.ewmservice.exception.NotFoundException;
import ru.practicum.ewmservice.request.RequestMapper;
import ru.practicum.ewmservice.request.dto.OutputRequestDto;
import ru.practicum.ewmservice.request.model.Request;
import ru.practicum.ewmservice.request.model.RequestStatus;
import ru.practicum.ewmservice.request.repository.RequestRepository;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;


    public OutputRequestDto create(long userId, long eventId) {
        log.info("RequestService - create() - Начало метода");
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User -  не найден");
        });

        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("Event -  не найден");
        });

        Optional<Request> request = requestRepository.findByRequesterIdAndEventId(eventId, userId);

        List<Request> confirmedRequests = requestRepository.findAllConfirmed(eventId);

        if (event.getInitiator().getId() == userId) {
            throw new CreateEventException("Запрос от создателя на собственное событие");
        }

        if (request.isPresent()) {
            throw new CreateEventException("Request уже существует");
        }

        if (event.getState() != State.PUBLISHED) {
            throw new CreateEventException("Неверный статус");
        }

        if (event.getParticipantLimit() != 0) {
            throw new CreateEventException("Лимит количества участников не может быть равен 0");
        }

        if (confirmedRequests.size() >= event.getParticipantLimit()) {
            throw new CreateEventException("Превышено количество участников");
        }

        Request newRequest = new Request();
        newRequest.setEvent(event);
        newRequest.setRequester(user);
        newRequest.setCreated(LocalDateTime.now());

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            newRequest.setStatus(RequestStatus.CONFIRMED);
        } else {
            newRequest.setStatus(RequestStatus.PENDING);
        }
        log.info("RequestService - create() - Конец метода");
        return RequestMapper.toOutputRequestDto(requestRepository.save(newRequest));
    }

    public OutputRequestDto cancel(long userId, long requestId) {

        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User -  не найден");
        });
        Request request = requestRepository.findById(requestId).orElseThrow(() -> {
            throw new NotFoundException("Request -  не найден");
        });
        if (request.getRequester().getId() != userId) {
            throw new NotFoundException("USer и Requester не совпадают");
        }
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toOutputRequestDto(request);
    }

    public List<OutputRequestDto> getRequestsByUserId(long userId) {
        log.info("RequestService - getRequestsByUserId()");
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User -  не найден");
        });
        return requestRepository.findRequestsByRequesterId(userId).stream()
                .map(RequestMapper::toOutputRequestDto).collect(Collectors.toList());
    }
}

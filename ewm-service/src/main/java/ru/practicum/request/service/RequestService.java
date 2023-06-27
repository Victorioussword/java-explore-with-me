package ru.practicum.request.service;


import java.util.List;
import java.time.LocalDateTime;
import ru.practicum.utils.Utils;
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

        Utils.checkRequest(event, userId, requestRepository);

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
}
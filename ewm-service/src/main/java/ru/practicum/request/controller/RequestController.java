package ru.practicum.request.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.service.RequestService;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class RequestController {

    private final RequestService requestService;


    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createRequest(@PathVariable Long userId,
                                    @RequestParam Long eventId) {

        RequestDto requestDto = requestService.createRequest(userId, eventId);
        log.info(" RequestController  - createRequest().  Создано   {}. ", requestDto.toString());
        return requestDto;
    }


    @GetMapping("/{userId}/requests")
    public List<RequestDto> getRequests(@PathVariable Long userId) {
        List<RequestDto> list = requestService.getRequests(userId);
        log.info(" RequestController  - getRequests.   Возвращено {}. ", list.size());
        return list;
    }


    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public RequestDto update(@PathVariable Long userId, @PathVariable Long requestId) {
        RequestDto requestDto = requestService.update(userId, requestId);
        log.info(" RequestController  - getRequests.   Обновлено {}. ", requestDto.toString());
        return requestDto;
    }
}
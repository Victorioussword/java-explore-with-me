package ru.practicum.ewmservice.request.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.ewmservice.request.dto.OutputRequestDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import ru.practicum.ewmservice.request.service.RequestService;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users/{userId}/requests")
public class RequestController {
    private final RequestService requestService;

    @GetMapping
    public List<OutputRequestDto> getRequestsByUserId(@PathVariable long userId) {
        log.info("RequestController - getRequestsByUserId()");
        return requestService.getRequestsByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OutputRequestDto create(@PathVariable long userId,
                                   @RequestParam long eventId) {
        log.info("RequestController - create().");
        return requestService.create(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public OutputRequestDto cancel(@PathVariable long userId,
                                   @PathVariable long requestId) {
        log.info("RequestController - cancel()");
        return requestService.cancel(userId, requestId);
    }
}
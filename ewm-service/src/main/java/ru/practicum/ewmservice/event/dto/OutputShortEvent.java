package ru.practicum.ewmservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.ewmservice.category.dto.CategoryDto;
import ru.practicum.ewmservice.user.model.UserShort;

import java.time.LocalDateTime;

public class OutputShortEvent {

    private Long id;

    private String annotation;

    private CategoryDto category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShort initiator;

    private long confirmedRequests;

    private boolean paid;

    private String title;

    private long views;
}

package ru.practicum.ewmservice.event.dto.ForEventContollerUser;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import ru.practicum.ewmservice.category.dto.CategoryDto;
import ru.practicum.ewmservice.user.model.UserShort;

import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OutputEventShortDto {

    private Long id;

    private String annotation;

    private CategoryDto category;

    private long confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShort initiator;

    private boolean paid;

    private String title;

    private long views;


}
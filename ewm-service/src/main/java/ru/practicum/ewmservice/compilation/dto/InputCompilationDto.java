package ru.practicum.ewmservice.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.util.List;


@Setter
@Getter
@AllArgsConstructor
public class InputCompilationDto {

    private String title;

    private List<Long> events;

    private Boolean pinned;
}

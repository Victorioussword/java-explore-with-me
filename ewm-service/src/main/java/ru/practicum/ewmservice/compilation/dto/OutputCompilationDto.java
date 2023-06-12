package ru.practicum.ewmservice.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewmservice.event.dto.ForEventContollerUser.OutputEventShortDto;


import java.util.List;


@Setter
@Getter
@AllArgsConstructor
public class OutputCompilationDto {

    private Long id;

    private String title;

    private List<OutputEventShortDto> events;

    private Boolean pinned;
}

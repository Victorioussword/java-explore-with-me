package ru.practicum.compilation.dto;


import lombok.Data;

import java.util.List;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.event.dto.EventShortDto;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OutputCompilationDto {

    private Long id;

    private String title;

    private List<EventShortDto> events;

    private Boolean pinned;
}
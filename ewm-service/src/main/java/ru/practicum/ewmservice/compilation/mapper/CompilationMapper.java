package ru.practicum.ewmservice.compilation.mapper;

import ru.practicum.ewmservice.compilation.dto.InputCompilationDto;
import ru.practicum.ewmservice.compilation.dto.OutputCompilationDto;
import ru.practicum.ewmservice.compilation.model.Compilation;
import ru.practicum.ewmservice.event.dto.ForEventContollerUser.OutputEventShortDto;
import ru.practicum.ewmservice.event.mapper.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {

    public static OutputCompilationDto toOutputCompilationDto(Compilation compilation) {
        List<OutputEventShortDto> shortEvents = compilation.getEvents().stream()
                .map(EventMapper::toOutputEventShortDto)
                .collect(Collectors.toList());

        return new OutputCompilationDto(
                compilation.getId(),
                compilation.getTitle(),
                shortEvents,
                compilation.getPinned());
    }

    public static Compilation toCompilation(InputCompilationDto inputCompilationDto) {
        return new Compilation(0L, inputCompilationDto.getTitle(), null, inputCompilationDto.getPinned());
    }
}
//OutputCompilationDto
//
//    private Long id;
//
//    private String title;
//
//    private List<Event> events;
//
//    private Boolean pinned;
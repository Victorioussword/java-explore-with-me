package ru.practicum.compilation.mapper;

import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;
import ru.practicum.event.Mapper.EventMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.dto.InputCompilationDto;
import ru.practicum.compilation.dto.OutputCompilationDto;


@UtilityClass
public class CompilationMapper {
    public static OutputCompilationDto toOutputCompilationDto(Compilation compilation) {
        return OutputCompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents().stream().map(EventMapper::toShortDto).collect(Collectors.toList()))
                .build();
    }


    public static Compilation toCompilation(InputCompilationDto inputCompilationDto) {
        return Compilation.builder()
                .title(inputCompilationDto.getTitle())
                .pinned(inputCompilationDto.getPinned())
                .build();
    }
}
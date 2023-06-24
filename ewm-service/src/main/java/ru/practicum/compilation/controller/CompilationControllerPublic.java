package ru.practicum.compilation.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.OutputCompilationDto;
import ru.practicum.compilation.service.CompilationService;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")

public class CompilationControllerPublic {

    private final CompilationService compilationService;


    @GetMapping("/{compId}")
    public OutputCompilationDto getById(@PathVariable Long compId) {

        OutputCompilationDto outputCompilationDto = compilationService.getById(compId);
        log.info("CompilationControllerPublic - getById().  Возвращен {} ", outputCompilationDto.toString());
        return outputCompilationDto;
    }

    @GetMapping
    public List<OutputCompilationDto> getAll(@RequestParam(defaultValue = "false") Boolean pinned,
                                             @RequestParam(name = "from", defaultValue = "0") int from,
                                             @RequestParam(name = "size", defaultValue = "10") int size) {

        List<OutputCompilationDto> list = compilationService.getAll(pinned, from, size);
        log.info("CompilationControllerPublic - getAll().  Возвращен списк из {} элементов", list.size());
        return list;
    }
}
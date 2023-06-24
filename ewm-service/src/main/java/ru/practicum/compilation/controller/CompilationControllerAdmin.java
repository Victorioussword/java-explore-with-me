package ru.practicum.compilation.controller;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.InputCompilationDto;
import ru.practicum.compilation.dto.OutputCompilationDto;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.compilation.dto.InputUpdateCompilationDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")

public class CompilationControllerAdmin {

    private final CompilationService compilationService;


    @PatchMapping("/{compId}")
    public OutputCompilationDto updateCompilation(@PathVariable Long compId,
                                                  @RequestBody @Valid InputUpdateCompilationDto inputUpdateCompilationDto) {

        OutputCompilationDto outputCompilationDto = compilationService.updateCompilation(inputUpdateCompilationDto, compId);
        log.info("CompilationControllerAdmin - updateCompilation.  Обновлено  {}", outputCompilationDto.toString());
        return outputCompilationDto;
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{compId}")
    public void delCompilation(@PathVariable Long compId) {
        log.info("CompilationControllerAdmin - delCompilation().  Удалена подборка  {}", compId);
        compilationService.delCompilation(compId);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OutputCompilationDto createCompilation(@RequestBody @Valid InputCompilationDto inputCompilationDto) {

        OutputCompilationDto outputCompilationDto = compilationService.createCompilation(inputCompilationDto);
        log.info("CompilationControllerAdmin - createCompilation().  ДОбавлено  {}", outputCompilationDto.toString());
        return outputCompilationDto;
    }
 }
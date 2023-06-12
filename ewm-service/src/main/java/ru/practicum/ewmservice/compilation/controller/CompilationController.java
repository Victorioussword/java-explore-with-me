package ru.practicum.ewmservice.compilation.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.compilation.dto.InputCompilationDto;
import ru.practicum.ewmservice.compilation.dto.OutputCompilationDto;
import ru.practicum.ewmservice.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class CompilationController {

    CompilationService compilationService;

    @PostMapping(path = "/admin/compilations")
    public OutputCompilationDto createComp(@RequestBody InputCompilationDto inputCompilationDto) {
        log.info("CompilationController - createComp(). Создан {}", inputCompilationDto.toString());
        return compilationService.crateComp(inputCompilationDto);
    }

    @DeleteMapping(path = "/admin/compilations/{compId}")
    public void deleteComp(@PathVariable Long compId) {

        log.info("CompilationController - deleteComp. Удален {}", compId);
        compilationService.deleteComp(compId);
    }


    @PatchMapping(path = " /admin/compilations/{compId}")
    public OutputCompilationDto patchComp(@PathVariable Long compId,
                                          @RequestBody InputCompilationDto inputCompilationDto) {

        log.info("CompilationController - patchComp(). Обновлен {}", inputCompilationDto);
        return compilationService.patchComp(inputCompilationDto, compId);
    }

    @GetMapping(path = "/compilations")
    public List<OutputCompilationDto> getAllComp(@RequestParam(required = false) Boolean pinned,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(defaultValue = "10") Integer size) {

        log.info("CompilationController -  getAllComp() ");
        return compilationService.getAllComp(pinned, from, size);
    }

    @GetMapping(path = "/compilations/{compId}")
    public OutputCompilationDto getComp(@PathVariable Long compId) {
        log.info("CompilationController -  getComp()");
        return compilationService.getComp(compId);
    }
}

//   get +  /compilations
//   get + /compilations/{compId}

//  post  +     /admin/compilations
//  del  +   /admin/compilations/{compId}
//  patch  + /admin/compilations/{compId}
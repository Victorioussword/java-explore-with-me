package ru.practicum.compilation.service;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import ru.practicum.event.model.Event;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.model.Compilation;
import org.springframework.data.domain.PageRequest;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exceptions.ObjectNotFoundException;
import ru.practicum.compilation.dto.InputCompilationDto;
import ru.practicum.compilation.dto.OutputCompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.dto.InputUpdateCompilationDto;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.repository.CompilationRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationService {

    private final CompilationRepository compilationRepository;

    private final EventRepository eventRepository;


    @Transactional
    public void delCompilation(Long id) {
        if (!compilationRepository.existsById(id)) {
            throw new ObjectNotFoundException("Compilation не найдена ");
        }
        log.info(" CompilationService - delCompilation. Удалена подборка {}", id);
        compilationRepository.deleteById(id);
    }


    public OutputCompilationDto getById(Long compId) {
        Optional<Compilation> optionalCompilation = compilationRepository.findById(compId);

        if (optionalCompilation.isEmpty()) {
            throw new ObjectNotFoundException("Compilation не существует");
        }
        OutputCompilationDto outputCompilationDto = CompilationMapper.toOutputCompilationDto(optionalCompilation.get());
        log.info(" CompilationService - getById().  Возвращена подборка {}", outputCompilationDto.toString());

        return outputCompilationDto;
    }


    @Transactional
    public OutputCompilationDto createCompilation(InputCompilationDto inputCompilationDto) {

        Compilation compilation = CompilationMapper.toCompilation(inputCompilationDto);

        if (inputCompilationDto.getPinned() == null) {
            compilation.setPinned(false);
        }
        if (inputCompilationDto.getEvents() == null) {
            compilation.setEvents(eventRepository.findAll());
        } else {
            List<Event> events = eventRepository.findAllById(inputCompilationDto.getEvents());
            compilation.setEvents(events);
        }
        log.info(" CompilationService - createCompilation(). Создана подборка {}", compilation.toString());
        return CompilationMapper.toOutputCompilationDto(compilationRepository.save(compilation));
    }


    public List<OutputCompilationDto> getAll(Boolean pinned, int from, int size) {

        List<OutputCompilationDto> dtos = compilationRepository.getAllByPinned(pinned, PageRequest.of(from, size)).stream()
                .map(CompilationMapper::toOutputCompilationDto)
                .collect(Collectors.toList());
        log.info(" CompilationService - getAll().  Возвращен список {}", dtos.size());
        return dtos;
    }


    @Transactional
    public OutputCompilationDto updateCompilation(InputUpdateCompilationDto inputUpdateCompilationDto, Long compId) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Подборка не найдена"));

        if (inputUpdateCompilationDto.getEvents() == null) {
            inputUpdateCompilationDto.setEvents(eventRepository.findAll().stream().map(Event::getId).collect(Collectors.toList()));
        }

        if (inputUpdateCompilationDto.getPinned() != null) {
            compilation.setPinned(inputUpdateCompilationDto.getPinned());
        }

        if (inputUpdateCompilationDto.getTitle() != null) {
            compilation.setTitle(inputUpdateCompilationDto.getTitle());
        }

        if (inputUpdateCompilationDto.getEvents().size() != 0) {
            List<Event> events = eventRepository.findAllById(inputUpdateCompilationDto.getEvents());
            compilation.setEvents(events);
        }

        log.info(" CompilationService - updateCompilation(). Обновлена подборка {}", compId);

        return CompilationMapper.toOutputCompilationDto(compilation);
    }
}
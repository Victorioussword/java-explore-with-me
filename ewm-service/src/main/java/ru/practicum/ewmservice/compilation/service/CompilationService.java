package ru.practicum.ewmservice.compilation.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.compilation.dto.InputCompilationDto;
import ru.practicum.ewmservice.compilation.dto.OutputCompilationDto;
import ru.practicum.ewmservice.compilation.mapper.CompilationMapper;
import ru.practicum.ewmservice.compilation.model.Compilation;
import ru.practicum.ewmservice.compilation.repository.CompilationRepository;


import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.repository.EventRepository;


import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public OutputCompilationDto crateComp(InputCompilationDto inputCompilationDto) {
        log.info("CompilationService -  crateComp(). Добавлен {}", inputCompilationDto.toString());

        List<Event> events = eventRepository.findEventsByIdIn(inputCompilationDto.getEvents());
//        List<OutputFullEventDto> outputFullEventDtos = events.stream().map(EventMapper::toOutputFullEventDto).collect(Collectors.toList());
        Compilation compilation = CompilationMapper.toCompilation(inputCompilationDto);
        compilation.setEvents(events);


        return CompilationMapper.toOutputCompilationDto(compilationRepository
                .save(compilation));
    }


    public void deleteComp(Long compId) {
        log.info("CompilationService -  deleteComp(). Удален {}", compId);
        Compilation compilation = compilationRepository.findById(compId).orElseThrow();
        compilationRepository.deleteById(compId);
    }


    public OutputCompilationDto patchComp(InputCompilationDto inputCompilationDto, Long compId) {

        log.info("CompilationService -  patchComp(). Обновлен {}", inputCompilationDto.toString());

        Compilation compilation = compilationRepository.findById(compId).orElseThrow();
        if (inputCompilationDto.getEvents() != null) {
            compilation.setEvents(eventRepository.findEventsByIdIn(inputCompilationDto.getEvents()));
        }
        if (inputCompilationDto.getPinned() != null) {
            compilation.setPinned(inputCompilationDto.getPinned());
        }
        if (inputCompilationDto.getTitle() != null) {
            compilation.setTitle(inputCompilationDto.getTitle());
        }
        // todo проверить как сохраняется в базу
        return CompilationMapper.toOutputCompilationDto(compilation);

    }


    public List<OutputCompilationDto> getAllComp(Boolean pinned, Integer from, Integer size) {
        log.info("CompilationService -  getAllComp(). Возвращен список");
        return compilationRepository.findAllByPinned(pinned, PageRequest.of(from, size))
                .stream()
                .map(CompilationMapper::toOutputCompilationDto)
                .collect(Collectors.toList());
    }


    public OutputCompilationDto getComp(Long compId) {
        log.info("CompilationService -  getComp().");
        Compilation compilation = compilationRepository.findById(compId).orElseThrow();
        return CompilationMapper.toOutputCompilationDto(compilation);
    }
}

package ru.practicum.ewmservice.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewmservice.category.dto.CategoryDto;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.event.dto.ForEventContollerUser.InputEventDto;
import ru.practicum.ewmservice.event.dto.ForEventContollerUser.OutputFullEventDto;
import ru.practicum.ewmservice.event.dto.ForEventContollerUser.OutputEventShortDto;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.State;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.model.UserShort;

import java.time.LocalDateTime;


@UtilityClass
public class EventMapper {


    public static Event toEvent(InputEventDto inputEventDto, Category category, User initiator) {

        return new Event(null,
                inputEventDto.getAnnotation(),
                category,
                inputEventDto.getEventDate(),
                initiator,
                inputEventDto.getPaid(),
                inputEventDto.getTitle(),
                inputEventDto.getDescription(),

                null,
                // TODO проверить как работает

                LocalDateTime.now(),
                inputEventDto.getLocation(),
                State.PENDING,
                inputEventDto.getParticipantLimit(),
                inputEventDto.getRequestModeration()
        );
    }


    public static OutputEventShortDto toEventShortDto2(OutputFullEventDto ev) {
        return new OutputEventShortDto(ev.getId(),
                ev.getAnnotation(),
                ev.getCategory(),
                ev.getConfirmedRequests(),
                ev.getEventDate(),
                ev.getInitiator(),
                ev.getPaid(),
                ev.getTitle(),
                ev.getViews());
    }

    public static OutputEventShortDto toOutputEventShortDto(Event ev) {
        return new OutputEventShortDto(ev.getId(),
                ev.getAnnotation(),
                new CategoryDto(ev.getCategory().getId(), ev.getCategory().getName()),
                                0L,
                ev.getEventDate(),
                new UserShort(ev.getInitiator().getId(), ev.getInitiator().getName()),
                ev.getPaid(),
                ev.getTitle(),
                0L);
    }

    public static OutputFullEventDto toOutputFullEventDto(Event ev) {
        return new OutputFullEventDto(
                ev.getId(),
                ev.getAnnotation(),
                new CategoryDto(ev.getCategory().getId(), ev.getCategory().getName()),
                0L,
                ev.getDescription(),
                ev.getEventDate(),
                new UserShort(ev.getInitiator().getId(), ev.getInitiator().getName()),
                ev.getLocation(),
                ev.getPaid(),
                ev.getParticipantLimit(),
                ev.getRequestModeration(),
                ev.getTitle(),
                0L,
                ev.getCreatedOn(),
                ev.getPublishedOn(),
                ev.getState()


        );
    }
}
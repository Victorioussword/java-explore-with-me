package ru.practicum.event.model;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@Setter
@Getter
public class CountRequests {

    long eventId;
    long countOfRequests;
}
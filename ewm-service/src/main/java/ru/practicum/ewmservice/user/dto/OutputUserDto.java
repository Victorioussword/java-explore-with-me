package ru.practicum.ewmservice.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class OutputUserDto {

    private Long id;

    private String name;

    private String email;

}

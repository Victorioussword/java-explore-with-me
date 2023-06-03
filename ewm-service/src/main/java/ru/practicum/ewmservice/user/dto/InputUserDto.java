package ru.practicum.ewmservice.user.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@Setter
@ToString
public class InputUserDto {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;
}

package ru.practicum.user.dto;

import lombok.*;

import javax.validation.constraints.Size;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserInputDto {

    private long id;

    @NotBlank
    @Size(min = 2, max = 250)
    private String name;

    @Size(min = 6, max = 254)
    @Email
    @NotBlank
    private String email;
}

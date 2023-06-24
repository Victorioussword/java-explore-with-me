package ru.practicum.category.dto;

import lombok.*;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InputСategoryDto {

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}

package ru.practicum.ewmservice.category.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@Setter
@ToString
public class InputCategoryDto {


    @NotBlank
    private String name;
}

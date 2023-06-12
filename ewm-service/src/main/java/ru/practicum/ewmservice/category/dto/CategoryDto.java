package ru.practicum.ewmservice.category.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Setter
@ToString
public class CategoryDto {

    private Long id;

    private String name;
}

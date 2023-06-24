package ru.practicum.category.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.category.model.Category;
import ru.practicum.category.dto.InputСategoryDto;
import ru.practicum.category.dto.OutputCategoryDto;


@UtilityClass
public class CategoryMapper {
    public static OutputCategoryDto toOutputCategoryDto(Category category) {
        return OutputCategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toCategory (InputСategoryDto inputСategoryDto) {
        return Category.builder()
                .name(inputСategoryDto.getName())
                .build();
    }
}

package ru.practicum.ewmservice.category.mapper;


import lombok.experimental.UtilityClass;
import ru.practicum.ewmservice.category.dto.CategoryDto;
import ru.practicum.ewmservice.category.dto.InputCategoryDto;
import ru.practicum.ewmservice.category.model.Category;

@UtilityClass
public class CategoryMapper {

    public static CategoryDto toCategoryDto(Category category) {

        return new CategoryDto(category.getId(), category.getName());
    }

    public static Category toCategory(InputCategoryDto inputCategoryDto) {
        return new Category(null, inputCategoryDto.getName());   // todo проверить как работает null
    }
}

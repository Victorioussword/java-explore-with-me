package ru.practicum.ewmservice.category.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.category.dto.CategoryDto;
import ru.practicum.ewmservice.category.dto.InputCategoryDto;

import ru.practicum.ewmservice.category.service.CategoryService;


import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class CategoryController {

    CategoryService categoryService;

    @PostMapping("admin/categories")
    public CategoryDto createCategory(@RequestBody InputCategoryDto inputCategoryDto) {
        log.info("CategoryController - createCategory(). ДОбавлена категория {}", inputCategoryDto.getName());
        return categoryService.createCategory(inputCategoryDto);
    }

    @DeleteMapping("admin/categories/{catId}")
    public void delCategory(@PathVariable long catId) {
        log.info("CategoryController - delCategory(). Удалена категория {}",catId);
        categoryService.delCategory(catId);
    }

    @PatchMapping("admin/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable long catId,
            @RequestBody InputCategoryDto inputCategoryDto
    ) {
        return categoryService.updateCategory(catId, inputCategoryDto);
    }


    @GetMapping("/categories/{catId}")
    public CategoryDto getById(@PathVariable long catId) {
        return categoryService.getById(catId);
    }



    @GetMapping("/categories")
    public List<CategoryDto> getAll(
            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
            @Positive @RequestParam(defaultValue = "10") int size
    ) {
        return categoryService.getAll(from, size);
    }


}

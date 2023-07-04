package ru.practicum.category.controller;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.InputCategoryDto;
import ru.practicum.category.dto.OutputCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.service.CategoryService;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")

public class CategoryControllerAdmin {

    private final CategoryService categoryService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OutputCategoryDto create(@RequestBody @Valid InputCategoryDto inputCategoryDto) {
        OutputCategoryDto outputCategoryDto = categoryService.create(CategoryMapper.toCategory(inputCategoryDto));
        log.info("CategoryControllerAdmin - createCategory().  ДОбавлено  {}", outputCategoryDto.toString());
        return outputCategoryDto;
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{catId}")
    public void del(@PathVariable Long catId) {

        log.info("CategoryControllerAdmin - delCategory().  Удалено Category id = {}", catId);
        categoryService.delete(catId);
    }


    @PatchMapping("/{catId}")
    public OutputCategoryDto update(@RequestBody @Valid InputCategoryDto inputCategoryDto,
                                    @PathVariable Long catId) {
        OutputCategoryDto outputCategoryDto = categoryService
                .update(inputCategoryDto, catId);
        log.info("CategoryControllerAdmin - update().  обновлена Category = {}", outputCategoryDto.getName());
        return outputCategoryDto;
    }
}

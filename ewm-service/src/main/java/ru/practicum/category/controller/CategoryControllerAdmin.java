package ru.practicum.category.controller;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import ru.practicum.category.model.Category;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.InputСategoryDto;
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
    public OutputCategoryDto create(@RequestBody @Valid InputСategoryDto inputСategoryDto) {
        OutputCategoryDto outputCategoryDto = categoryService.create(CategoryMapper.toCategory(inputСategoryDto));
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
    public OutputCategoryDto update(@RequestBody @Valid InputСategoryDto inputСategoryDto,
                                    @PathVariable Long catId) {
        OutputCategoryDto outputCategoryDto = categoryService
                .update(CategoryMapper.toCategory(inputСategoryDto), catId);
        log.info("CategoryControllerAdmin - update().  обновлена Category = {}", outputCategoryDto.getName());
        return outputCategoryDto;
    }
}

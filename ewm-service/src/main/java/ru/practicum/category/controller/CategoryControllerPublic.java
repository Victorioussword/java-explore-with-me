package ru.practicum.category.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.OutputCategoryDto;
import ru.practicum.category.service.CategoryService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoryControllerPublic {

    private final CategoryService categoryService;


    @GetMapping
    public List<OutputCategoryDto> getAllCategories(@RequestParam(name = "from", defaultValue = "0") int from,
                                                    @RequestParam(name = "size", defaultValue = "10") int size) {

        List<OutputCategoryDto> list = categoryService.getAllCategories(from, size);
        log.info("CategoryControllerPublic - getAllCategories().  Возвращен список из {} категорий", list.size());
        return list;
    }


    @GetMapping("/{catId}")
    public OutputCategoryDto getCategoryById(@PathVariable Long catId) {
        OutputCategoryDto outputCategoryDto = categoryService.getCategoryById(catId);
        log.info("CategoryControllerPublic - getCategoryById().  Возвращена категория {} категорий", outputCategoryDto.toString());
        return outputCategoryDto;
    }
}
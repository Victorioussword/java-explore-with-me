package ru.practicum.ewmservice.category.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.category.dto.CategoryDto;
import ru.practicum.ewmservice.category.dto.InputCategoryDto;
import ru.practicum.ewmservice.category.mapper.CategoryMapper;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.category.repository.CategoryRepository;
import ru.practicum.ewmservice.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    CategoryRepository categoryRepository;

    @Transactional
    public CategoryDto createCategory(InputCategoryDto inputCategoryDto) {

        CategoryDto categoryDto = CategoryMapper.toCategoryDto(categoryRepository
                .save(CategoryMapper.toCategory(inputCategoryDto)));
        log.info("CategoryService - createCategory(). Добавлен {}", categoryDto.toString());
        return categoryDto;
    }

    @Transactional
    public void delCategory(long catId) {
        log.info("CategoryService - delCategory(). Удалена категория № {}", catId);
        categoryRepository.deleteById(catId);
    }

    @Transactional
    public CategoryDto updateCategory(long catId, InputCategoryDto inputCategoryDto) {
        log.info("CategoryService - updateCategory(). обновлена категория №{}", catId);
        Category cat = categoryRepository.findById(catId).orElseThrow(() -> {
            throw new NotFoundException("Category не найдена");
        });
        cat.setName(inputCategoryDto.getName());  // todo проверить что cat уходит в б.д.
        return CategoryMapper.toCategoryDto(cat);
    }


    public CategoryDto getById(long catId) {
        log.info("CategoryService - getById(). Отпралена категория № {}", catId);
        Category cat = categoryRepository.findById(catId).orElseThrow(() -> {
            throw new NotFoundException("Category не найдена");
        });
        return CategoryMapper.toCategoryDto(cat);
    }


    public List<CategoryDto> getAll(int from, int size) {
        log.info("CategoryService - getAll()");
        Page<Category> categoryPage = categoryRepository.findAll(PageRequest.of(from, size));
        List<Category> categories = categoryPage.toList();
        List<CategoryDto> outputDtos = categories.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
        log.info("CategoryController - getAll(). Возвращен список из {} category", outputDtos.size());
        return outputDtos;
    }
}
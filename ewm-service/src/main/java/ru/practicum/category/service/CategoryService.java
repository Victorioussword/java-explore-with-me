package ru.practicum.category.service;


import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import ru.practicum.category.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.ConflictException;
import org.springframework.data.domain.PageRequest;
import ru.practicum.category.dto.OutputCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exceptions.ObjectNotFoundException;
import ru.practicum.category.repository.CategoryRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;


    public OutputCategoryDto create(Category category) {
        checkUniqueName(category.getName());
        log.info("CategoryService - create().  ДОбавлено  {}", category.toString());
        return CategoryMapper.toOutputCategoryDto(categoryRepository.save(category));
    }


    public List<OutputCategoryDto> getAllCategories(int from, int size) {
        Page<Category> categories = categoryRepository.findAll(PageRequest.of(from, size));
        log.info("CategoryService - getAllCategories().  Возвращен список из {}", categories.getSize());
        return categories.stream()
                .map(CategoryMapper::toOutputCategoryDto)
                .collect(Collectors.toList());
    }

    public Category findById(Long id) {
        Category cat = categoryRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("категория с таким id не существует"));
        log.info("CategoryService - findById().  Возвращен список из {}", cat.toString());
        return cat;
    }


    public void delete(Long id) {

        if (categoryRepository.findById(id).isEmpty()) {
            throw new ObjectNotFoundException("категория с таким id не существует");
        }
        if (eventRepository.countAllByCategoryId(id) != 0L) {
            throw new ConflictException("Невозможно удалить. Катеория используется");
        }
        categoryRepository.deleteById(id);
        log.info("CategoryService - delete().  Удалена категория id {}", id);
    }


    public OutputCategoryDto update(Category category, Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isEmpty()) {
            throw new ObjectNotFoundException("категория с таким id не существует");
        }

        if (!categoryOptional.get().getName().equals(category.getName())) {
            checkUniqueName(category.getName());
        }
        categoryOptional.get().setName(category.getName());
        log.info("CategoryService - update().  Обнолена категория  id = {}, name = {}",
                id, categoryOptional.get().getName());
        return CategoryMapper.toOutputCategoryDto(categoryOptional.get());
    }


    public OutputCategoryDto getCategoryById(Long id) {
        Category category = findById(id);
        log.info("CategoryService - getAllCategories().  Возвращен список из {}", category.toString());
        return CategoryMapper.toOutputCategoryDto(category);
    }


    private void checkUniqueName(String name) {
        log.info("CategoryService - checkUniqueName().  проверка имени {}", name);
        if (categoryRepository.findByName(name).isPresent()) {
            throw new ConflictException("Category с таким именем уже существует");
        }
    }
}
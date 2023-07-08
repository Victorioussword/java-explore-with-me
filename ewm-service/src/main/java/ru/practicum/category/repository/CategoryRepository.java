package ru.practicum.category.repository;

import java.util.Optional;

import ru.practicum.category.model.Category;

import org.springframework.data.jpa.repository.JpaRepository;



public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

}
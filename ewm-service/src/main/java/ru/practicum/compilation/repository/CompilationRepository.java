package ru.practicum.compilation.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;

import ru.practicum.compilation.model.Compilation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    List<Compilation> getAllByPinned(Boolean pinned, Pageable pageable);

}
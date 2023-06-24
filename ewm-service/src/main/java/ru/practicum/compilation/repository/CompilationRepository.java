package ru.practicum.compilation.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.compilation.model.Compilation;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    List<Compilation> getAllByPinned(Boolean pinned, Pageable pageable);

}
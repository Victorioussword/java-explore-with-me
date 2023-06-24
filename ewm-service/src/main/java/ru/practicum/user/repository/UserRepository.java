package ru.practicum.user.repository;


import java.util.List;
import java.util.Optional;

import ru.practicum.user.model.User;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByName(String name);

    List<User> findByIdIn(List<Long> ids, Pageable pageable);
}
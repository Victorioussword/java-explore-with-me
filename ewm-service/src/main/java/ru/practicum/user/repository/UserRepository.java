package ru.practicum.user.repository;


import java.util.List;

import ru.practicum.user.model.User;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByIdIn(List<Long> ids, Pageable pageable);

    List<User> findAllByName(String name);
}
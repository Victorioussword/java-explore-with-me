package ru.practicum.user.service;


import java.util.List;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

import ru.practicum.exceptions.ConflictException;
import ru.practicum.user.model.User;
import ru.practicum.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import ru.practicum.user.dto.UserInputDto;
import ru.practicum.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.exceptions.ObjectNotFoundException;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto create(UserInputDto userInputDto) {
       // Тест Postman: "Добавление пользователя с занятым именем"
        if (!userRepository.findAllByName(userInputDto.getName()).isEmpty()) {
            throw new ConflictException("User c таки именем уже существует");
        }

        User user = UserMapper.toUser(userInputDto);

        log.info("UserService - create().  Создано  {}", user.toString());

        return UserMapper.toUserDto(userRepository.save(user));
    }

    public void del(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ObjectNotFoundException("User не найден ");
        }
        log.info("UserService - del().  Удален user id = {} ", id);
        userRepository.deleteById(id);
    }

    public List<UserDto> getAll(List<Long> ids, int from, int size) {

        if (ids == null) {
            log.info("UserService - getAll().  Возвращены все User");
            return userRepository.findAll(PageRequest.of(from, size))
                    .stream().map(UserMapper::toUserDto).collect(Collectors.toList());

        } else {
            log.info("UserService - getAll().  Возвращен список из {} элементов", ids.size());
            return userRepository.findByIdIn(ids, PageRequest.of(from, size))
                    .stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        }
    }
}
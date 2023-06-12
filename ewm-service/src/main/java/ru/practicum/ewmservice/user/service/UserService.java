package ru.practicum.ewmservice.user.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.exception.ValidationException;
import ru.practicum.ewmservice.user.dto.InputUserDto;
import ru.practicum.ewmservice.user.dto.OutputUserDto;
import ru.practicum.ewmservice.user.mapper.UserMapper;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public OutputUserDto createUser(InputUserDto inputUserDto) {
        OutputUserDto outputUserDto = UserMapper.toOutputUserDto(userRepository.save(UserMapper.toUser(inputUserDto)));
        log.info("UserService - createUser(). Добавлен {}", outputUserDto.toString());
        return outputUserDto;
    }

    @Transactional
    public void delById(Long id) {
        checkUnicId(id);
        log.info("UserService - delById(). Удален пользователь с id {}", id);
        userRepository.deleteById(id);

    }

    public List<OutputUserDto> getUsersByIds(List<Long> usersIds, Integer from, Integer size) {

        if (usersIds == null || usersIds.isEmpty()) {
            Page<User> usersPage = userRepository.findAll(PageRequest.of(from, size));
            List<User> users = usersPage.toList();
            List<OutputUserDto> outputUserDtos = users.stream().map(UserMapper::toOutputUserDto).collect(Collectors.toList());
            log.info("UserController - getAll(). Возвращен список из {} пользователей", outputUserDtos.size());
            return outputUserDtos;

        } else {
            Page<User> usersPage = userRepository.findAllByIdIn(usersIds, PageRequest.of(from, size));
            List<User> users = usersPage.toList();
            List<OutputUserDto> outputUserDtos = users.stream().map(UserMapper::toOutputUserDto).collect(Collectors.toList());
            log.info("UserController - getAll(). Возвращен список из {} пользователей", outputUserDtos.size());
            return outputUserDtos;
        }
    }

    private void checkUnicId(long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new ValidationException("Пользователь с таким id не существует");
        }
    }
}


//        ge<User> usersPage = userRepository.findAll(PageRequest.of(from, size));
//        List<User> users = usersPage.toList();
//        List<UserDto> userDtos = users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
//        log.info("UserController - getAll(). Возвращен список из {} пользователей", userDtos.size());
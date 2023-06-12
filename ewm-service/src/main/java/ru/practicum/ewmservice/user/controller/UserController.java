package ru.practicum.ewmservice.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.user.dto.InputUserDto;
import ru.practicum.ewmservice.user.dto.OutputUserDto;
import ru.practicum.ewmservice.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public OutputUserDto createUser(@RequestBody InputUserDto inputUserDto) {
        log.info("UserController - createUser(). Создан {}", inputUserDto.toString());
        return userService.createUser(inputUserDto);
    }

    @DeleteMapping("/{id}")
    public void delById(@PathVariable Long id) {
        log.info("UserController - delById(). Удален пользователь с id {}", id);
        userService.delById(id);
    }

    @GetMapping
    public List<OutputUserDto> getUsersByIds(
            @RequestParam(name = "ids", required = false) List<Long> usersIds,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        return userService.getUsersByIds(usersIds, from, size);
    }

}

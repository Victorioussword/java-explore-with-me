package ru.practicum.user.controller;

import java.util.List;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import ru.practicum.user.dto.UserInputDto;
import org.springframework.http.HttpStatus;
import ru.practicum.user.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")

public class UserController {

    private final UserService userService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Validated @Valid UserInputDto userInputDto) {
        UserDto userDto = userService.create(userInputDto);
        log.info("UserController - create().  Создано  {}", userDto.toString());
        return userDto;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delUser(@PathVariable Long userId) {
        log.info("UserController - delUser().  Удален User Id = {}", userId);
        userService.del(userId);
    }


    @GetMapping
    public List<UserDto> getAll(@RequestParam(required = false) List<Long> ids,
                                @RequestParam(name = "from", defaultValue = "0") int from,
                                @RequestParam(name = "size", defaultValue = "10") int size) {
        List<UserDto> dtos = userService.getAll(ids, from, size);
        log.info("UserController - getAll ().  Возвращен список User  из {} элементов", dtos.size());
        return dtos;
    }
}
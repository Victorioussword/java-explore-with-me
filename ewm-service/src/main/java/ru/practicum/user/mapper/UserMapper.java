package ru.practicum.user.mapper;

import ru.practicum.user.model.User;
import ru.practicum.user.dto.UserDto;
import lombok.experimental.UtilityClass;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.dto.UserInputDto;


@UtilityClass
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static User toUser(UserInputDto userInputDto) {
        return User.builder()
                .name(userInputDto.getName())
                .email(userInputDto.getEmail())
                .build();
    }
}

package ru.practicum.ewmservice.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewmservice.user.dto.InputUserDto;
import ru.practicum.ewmservice.user.dto.OutputUserDto;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.model.UserShort;


@UtilityClass
public class UserMapper {


   public static OutputUserDto toOutputUserDto(User user) {
        return new OutputUserDto(user.getId(), user.getName(), user.getEmail());
    }


    public static User toUser(InputUserDto inputUserDto) {

        return new User(null, inputUserDto.getName(), inputUserDto.getEmail());  // todo проверить как работает null
    }

    public static UserShort toUserShort(User user) {
        return new UserShort(user.getId(), user.getName());
    }
}

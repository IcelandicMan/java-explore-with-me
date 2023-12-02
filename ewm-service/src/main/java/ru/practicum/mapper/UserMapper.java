package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.user.UserRequestDto;
import ru.practicum.dto.user.UserResponseDto;
import ru.practicum.dto.user.UserResponseShortDto;
import ru.practicum.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static User userRequestDtoToUser(UserRequestDto requestUser) {
        return User.builder()
                .name(requestUser.getName())
                .email(requestUser.getEmail())
                .build();
    }

    public static UserResponseDto userToUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserResponseShortDto userToUserResponseShortDto(User user) {
        return UserResponseShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }


}

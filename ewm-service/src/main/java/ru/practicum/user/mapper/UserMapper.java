package ru.practicum.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.user.dto.UserRequestDto;
import ru.practicum.user.dto.UserResponseDto;
import ru.practicum.user.dto.UserResponseShortDto;
import ru.practicum.user.model.User;

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

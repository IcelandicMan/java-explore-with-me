package ru.practicum.service.user;

import ru.practicum.dto.user.UserRequestDto;
import ru.practicum.dto.user.UserResponseDto;

import java.util.List;

public interface UserService {
    public UserResponseDto createUser(UserRequestDto user);

    public List<UserResponseDto> getUsers(List<Long> usersId, Integer from, Integer size);

    public UserResponseDto updateUser(Long userId, UserRequestDto updatedUser);

    public void deleteUser(Long id);
}

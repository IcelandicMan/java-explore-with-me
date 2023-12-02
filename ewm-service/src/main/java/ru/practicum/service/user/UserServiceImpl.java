package ru.practicum.service.user;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.user.UserRequestDto;
import ru.practicum.dto.user.UserResponseDto;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.util.UnionService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UnionService unionService;

    @Override
    public UserResponseDto createUser(UserRequestDto user) {
        User createdUser = UserMapper.userRequestDtoToUser(user);
        return UserMapper.userToUserResponseDto(userRepository.save(createdUser));
    }

    @Override
    public List<UserResponseDto> getUsers(List<Long> usersIds, Integer from, Integer size) {
        List<User> usersList = new ArrayList<>();
        Pageable pageable = PageRequest.of(from / size, size);
        if (usersIds == null || usersIds.isEmpty()) {
            usersList = userRepository.findAllUsers(pageable);
        } else {
            usersList = userRepository.findUsersByIds(usersIds, pageable);
        }
        return usersList.stream()
                .map(UserMapper::userToUserResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto updateUser(Long userId, UserRequestDto updatedUser) {
        User user = unionService.getUserOrNotFound(userId);
        final String email = updatedUser.getEmail();
        final String updatedName = updatedUser.getName();
        user.setId(userId);
        if (email != null) {
            user.setEmail(email);
        }
        if (updatedName != null) {
            user.setName(updatedName);
        }
        return UserMapper.userToUserResponseDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        unionService.getUserOrNotFound(id);
        userRepository.deleteById(id);
    }
}

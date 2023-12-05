package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserRequestDto;
import ru.practicum.user.dto.UserResponseDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserAdminController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto user) {
        log.info("Запрошено создание пользователя: {} ", user);
        UserResponseDto createdUser = userService.createUser(user);
        log.info("Запрос на создание пользователя выполнен, пользователь создан: {} ", createdUser);
        return createdUser;
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<UserResponseDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                          @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                          @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Запрошен список пользователей с id {} c параметрами from {}, size {}", ids, from, size);
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("userId") Long userId) {
        log.info("Запрошено удаление пользователя с id {} ", userId);
        userService.deleteUser(userId);
        log.info("Запрос на удаление пользователя id {} выполнен", userId);
    }
}


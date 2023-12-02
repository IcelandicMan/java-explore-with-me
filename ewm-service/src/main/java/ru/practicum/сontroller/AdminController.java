package ru.practicum.сontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryRequestDto;
import ru.practicum.dto.category.CategoryResponseDto;
import ru.practicum.dto.event.EventRequestUpdateDto;
import ru.practicum.dto.event.EventResponseFullDto;
import ru.practicum.dto.user.UserRequestDto;
import ru.practicum.dto.user.UserResponseDto;
import ru.practicum.mapper.compilation.CompilationRequestNewDto;
import ru.practicum.mapper.compilation.CompilationRequestUpdateDto;
import ru.practicum.mapper.compilation.CompilationResponseDto;
import ru.practicum.service.category.CategoryService;
import ru.practicum.service.compilation.CompilationService;
import ru.practicum.service.event.EventService;
import ru.practicum.service.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin")
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;

    @PostMapping("/users") // 1
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto user) {
        log.info("Запрошено создание пользователя: {} ", user);
        UserResponseDto createdUser = userService.createUser(user);
        log.info("Запрос на создание пользователя выполнен, пользователь создан: {} ", createdUser);
        return createdUser;
    }

    @GetMapping("/users") // 2
    @ResponseStatus(value = HttpStatus.OK)
    public List<UserResponseDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                          @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                          @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Запрошен список пользователей с id {} c параметрами from {}, size {}", ids, from, size);
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/users/{userId}") // 3
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("userId") Long userId) {
        log.info("Запрошено удаление пользователя с id {} ", userId);
        userService.deleteUser(userId);
        log.info("Запрос на удаление пользователя id {} выполнен", userId);
    }

    @PostMapping("/categories") // 4
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@Valid @RequestBody CategoryRequestDto category) {
        log.info("Запрошено создание категории: {} ", category);
        CategoryResponseDto createdCategory = categoryService.createCategory(category);
        log.info("Запрос на создание категории выполнен, категория создана: {} ", createdCategory);
        return createdCategory;
    }

    @PatchMapping("/categories/{catId}") // 5
    @ResponseStatus(value = HttpStatus.OK)
    public CategoryResponseDto updateCategory(@PathVariable Long catId,
                                              @Valid @RequestBody CategoryRequestDto category) {
        log.info("Запрошено обновление категории под id: {} ", catId);
        CategoryResponseDto updatedCategory = categoryService.updateCategory(catId, category);
        log.info("Запрос выполнен, категория обновлена: {} ", updatedCategory);
        return updatedCategory;
    }

    @DeleteMapping("/categories/{catId}") // 6
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("catId") Long catId) {
        log.info("Запрошено удаление категории с id {} ", catId);
        categoryService.deleteCategory(catId);
        log.info("Запрос на удаление категории с id {} выполнен", catId);
    }

    @GetMapping("/events") // 7
    @ResponseStatus(value = HttpStatus.OK)
    public List<EventResponseFullDto> getEventsByAdmin(@RequestParam(required = false, name = "users") List<Long> users,
                                                       @RequestParam(required = false, name = "states") List<String> states,
                                                       @RequestParam(required = false, name = "categories") List<Long> categories,
                                                       @RequestParam(required = false, name = "rangeStart")
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                       @RequestParam(required = false, name = "rangeEnd")
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                       @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                       @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("Получение событий от Админа по следующим параметрам: users = {}, states = {}, categories = {}, " +
                        "rangeStart = {}, rangeEnd = {}, from = {}, size = {}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        List<EventResponseFullDto> events = eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
        log.info("Информация предоставлена: {} ", events);
        return events;
    }

    @PatchMapping("/events/{eventId}") // 8
    @ResponseStatus(value = HttpStatus.OK)
    public EventResponseFullDto updateEventByAdmin(@Valid @RequestBody EventRequestUpdateDto eventUpdateDto,
                                                   @PathVariable Long eventId) {
        log.info("Запрос от Админа на обновление события с id {}: {} ", eventId, eventUpdateDto);
        EventResponseFullDto event = eventService.updateEventByAdmin(eventUpdateDto, eventId);
        log.info("Запрос от Админа на обновление события с id {} выполнен, событие обновлено : {} ", eventId, event);
        return event;
    }

    @PostMapping("/compilations") // 9
    @ResponseStatus(value = HttpStatus.CREATED)
    public CompilationResponseDto createCompilation(@Valid @RequestBody CompilationRequestNewDto compilationRequestUpdateDto) {
        log.info("Запрошено создание подборки событий: {} ", compilationRequestUpdateDto);
        CompilationResponseDto compilationResponseDto = compilationService.createCompilation(compilationRequestUpdateDto);
        log.info("Запрос на создание подборки событий выполнен: {} ", compilationRequestUpdateDto);
        return compilationResponseDto;
    }

    @DeleteMapping("/compilations/{compId}") // 10
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Запрошено подборки событий с id {} ", compId);
        compilationService.deleteCompilation(compId);
        log.info("Запрос на удаление подборки событий с id {} выполнен", compId);
    }

    @PatchMapping("/compilations/{compId}") // 11
    @ResponseStatus(value = HttpStatus.OK)
    public CompilationResponseDto updateCompilationByAdmin(@Valid @RequestBody CompilationRequestUpdateDto compilationRequestUpdateDto,
                                                           @PathVariable Long compId) {
        log.info("Запрос от Админа на обновление подборки событий с id {}: {} ", compId, compilationRequestUpdateDto);
        CompilationResponseDto compilation = compilationService.updateCompilation(compId, compilationRequestUpdateDto);
        log.info("Запрос от Админа на обновление  подборки событий с id {} выполнен: {} ", compId, compilation);
        return compilation;
    }

}

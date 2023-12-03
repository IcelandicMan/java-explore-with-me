package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryRequestDto;
import ru.practicum.category.dto.CategoryResponseDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@Valid @RequestBody CategoryRequestDto category) {
        log.info("Запрошено создание категории: {} ", category);
        CategoryResponseDto createdCategory = categoryService.createCategory(category);
        log.info("Запрос на создание категории выполнен, категория создана: {} ", createdCategory);
        return createdCategory;
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CategoryResponseDto updateCategory(@PathVariable Long catId,
                                              @Valid @RequestBody CategoryRequestDto category) {
        log.info("Запрошено обновление категории под id: {} ", catId);
        CategoryResponseDto updatedCategory = categoryService.updateCategory(catId, category);
        log.info("Запрос выполнен, категория обновлена: {} ", updatedCategory);
        return updatedCategory;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("catId") Long catId) {
        log.info("Запрошено удаление категории с id {} ", catId);
        categoryService.deleteCategory(catId);
        log.info("Запрос на удаление категории с id {} выполнен", catId);
    }
}

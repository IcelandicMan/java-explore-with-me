package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryRequestDto;
import ru.practicum.category.dto.CategoryResponseDto;

import java.util.List;

public interface CategoryService {

    public CategoryResponseDto createCategory(CategoryRequestDto category);

    public CategoryResponseDto getCategory(Long id);

    public List<CategoryResponseDto> getCategories(Integer from, Integer size);

    public CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto category);

    public void deleteCategory(Long id);

}

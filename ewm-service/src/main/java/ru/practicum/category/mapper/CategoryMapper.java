package ru.practicum.category.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryRequestDto;
import ru.practicum.category.dto.CategoryResponseDto;
import ru.practicum.category.model.Category;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {

    public static Category categoryRequestDtoToCategory(CategoryRequestDto requestCategory) {
        return Category.builder()
                .name(requestCategory.getName())
                .build();

    }

    public static CategoryResponseDto categoryToCategoryResponseDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}

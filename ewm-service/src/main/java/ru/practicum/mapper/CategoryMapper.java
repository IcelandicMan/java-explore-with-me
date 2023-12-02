package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.category.CategoryRequestDto;
import ru.practicum.dto.category.CategoryResponseDto;
import ru.practicum.model.Category;

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

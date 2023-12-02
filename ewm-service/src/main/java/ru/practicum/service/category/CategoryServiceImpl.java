package ru.practicum.service.category;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryRequestDto;
import ru.practicum.dto.category.CategoryResponseDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.util.UnionService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UnionService unionService;

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto category) {
        Category createdCategory = CategoryMapper.categoryRequestDtoToCategory(category);
        return CategoryMapper.categoryToCategoryResponseDto(categoryRepository.save(createdCategory));
    }

    @Override
    public CategoryResponseDto getCategory(Long id) {
        Category category = unionService.getCategoryOrNotFound(id);
        return CategoryMapper.categoryToCategoryResponseDto(category);
    }

    @Override
    public List<CategoryResponseDto> getCategories(Integer from, Integer size) {
        List<Category> categoryList = new ArrayList<>();
        Pageable pageable = PageRequest.of(from / size, size);
        categoryList = categoryRepository.findCategories(pageable);
        return categoryList.stream()
                .map(CategoryMapper::categoryToCategoryResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto category) {
        Category updatedCategory = unionService.getCategoryOrNotFound(categoryId);
        final String updatedName = category.getName();
        if (updatedName != null) {
            updatedCategory.setName(updatedName);
        }
        return CategoryMapper.categoryToCategoryResponseDto(categoryRepository.save(updatedCategory));
    }

    @Override
    public void deleteCategory(Long id) {
        unionService.getCategoryOrNotFound(id);
        if (!eventRepository.findByCategoryId(id).isEmpty()) {
            throw new ConflictException(String.format("Категория с id %s используется и не может быть удалена", id));
        }
        categoryRepository.deleteById(id);
    }
}

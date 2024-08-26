package ru.practicum.category.sevice;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;


public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto dto);

    void delete(long catId);

    CategoryDto updateCategory(CategoryDto dto);

    List<CategoryDto> getAll(int from, int size);

    CategoryDto getById(long catId);
}

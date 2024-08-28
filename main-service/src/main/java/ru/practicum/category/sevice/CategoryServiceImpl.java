package ru.practicum.category.sevice;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.service.EventService;
import ru.practicum.exeption.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto dto) {
        Category category = categoryMapper.toCategory(dto);

        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void delete(long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id= " + catId + " was not found"));
//        TODO 409 Существуют события, связанные с категорией --- если категория имеет события, то ее нельзя удалить

        categoryRepository.deleteById(catId);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto dto) {
        Category category = categoryRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Category with id= " + dto.getId() + " was not found"));
        category.setName(dto.getName());

        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size);
        Page<Category> categoriesPage = categoryRepository.findAll(pageable);

        if (categoriesPage.hasContent()) {
            return categoriesPage.getContent().stream()
                    .map(categoryMapper::toCategoryDto)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public CategoryDto getById(long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id= " + catId + " was not found"));

        return categoryMapper.toCategoryDto(category);
    }
}

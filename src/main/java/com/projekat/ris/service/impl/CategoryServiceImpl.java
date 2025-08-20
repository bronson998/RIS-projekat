package com.projekat.ris.service.impl;

import com.projekat.ris.dto.CategoryDTO;
import com.projekat.ris.dto.mappers.CategoryMapper;
import com.projekat.ris.model.Category;
import com.projekat.ris.repository.CategoryRepository;
import com.projekat.ris.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Category Found"));

        return categoryMapper.toDTO(category);
    }

    @Override
    public CategoryDTO getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("No Category Found"));

        return categoryMapper.toDTO(category);
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Category category = categoryMapper.toEntity(categoryDTO);

        return categoryMapper.toDTO(categoryRepository.save(category));
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(categoryDTO.getId())
                .orElseThrow(() -> new RuntimeException("No Category Found"));

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        Category updated = categoryRepository.save(category);
        return categoryMapper.toDTO(updated);
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }
}

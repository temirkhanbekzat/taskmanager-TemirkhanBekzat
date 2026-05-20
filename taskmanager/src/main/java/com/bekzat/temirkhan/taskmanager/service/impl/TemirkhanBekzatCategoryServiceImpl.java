package com.bekzat.temirkhan.taskmanager.service.impl;

import com.bekzat.temirkhan.taskmanager.dto.request.TemirkhanBekzatCategoryRequest;
import com.bekzat.temirkhan.taskmanager.dto.response.TemirkhanBekzatCategoryResponse;
import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatCategory;
import com.bekzat.temirkhan.taskmanager.exception.TemirkhanBekzatBadRequestException;
import com.bekzat.temirkhan.taskmanager.exception.TemirkhanBekzatResourceNotFoundException;
import com.bekzat.temirkhan.taskmanager.mapper.TemirkhanBekzatCategoryMapper;
import com.bekzat.temirkhan.taskmanager.repository.TemirkhanBekzatCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemirkhanBekzatCategoryServiceImpl {

    private final TemirkhanBekzatCategoryRepository categoryRepository;
    private final TemirkhanBekzatCategoryMapper categoryMapper;

    @Transactional
    public TemirkhanBekzatCategoryResponse createCategory(TemirkhanBekzatCategoryRequest request) {
        log.info("Creating category: {}", request.getName());
        if (categoryRepository.existsByName(request.getName())) {
            throw new TemirkhanBekzatBadRequestException("Category already exists: " + request.getName());
        }
        TemirkhanBekzatCategory category = TemirkhanBekzatCategory.builder()
                .name(request.getName())
                .color(request.getColor())
                .description(request.getDescription())
                .build();
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    public List<TemirkhanBekzatCategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    public TemirkhanBekzatCategoryResponse getCategoryById(Long id) {
        TemirkhanBekzatCategory cat = categoryRepository.findById(id)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("Category not found: " + id));
        return categoryMapper.toResponse(cat);
    }

    @Transactional
    public TemirkhanBekzatCategoryResponse updateCategory(Long id, TemirkhanBekzatCategoryRequest request) {
        TemirkhanBekzatCategory cat = categoryRepository.findById(id)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("Category not found: " + id));
        cat.setName(request.getName());
        if (request.getColor() != null) cat.setColor(request.getColor());
        if (request.getDescription() != null) cat.setDescription(request.getDescription());
        return categoryMapper.toResponse(categoryRepository.save(cat));
    }

    @Transactional
    public void deleteCategory(Long id) {
        TemirkhanBekzatCategory cat = categoryRepository.findById(id)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("Category not found: " + id));
        categoryRepository.delete(cat);
    }
}

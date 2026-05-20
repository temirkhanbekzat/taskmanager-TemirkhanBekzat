package com.bekzat.temirkhan.taskmanager.controller;

import com.bekzat.temirkhan.taskmanager.dto.request.TemirkhanBekzatCategoryRequest;
import com.bekzat.temirkhan.taskmanager.dto.response.TemirkhanBekzatCategoryResponse;
import com.bekzat.temirkhan.taskmanager.service.impl.TemirkhanBekzatCategoryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Category management endpoints")
public class TemirkhanBekzatCategoryController {

    private final TemirkhanBekzatCategoryServiceImpl categoryService;

    @PostMapping
    @Operation(summary = "Create a new category")
    public ResponseEntity<TemirkhanBekzatCategoryResponse> createCategory(
            @Valid @RequestBody TemirkhanBekzatCategoryRequest request) {
        log.info("POST /api/categories");
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request));
    }

    @GetMapping
    @Operation(summary = "Get all categories")
    public ResponseEntity<List<TemirkhanBekzatCategoryResponse>> getAllCategories() {
        log.info("GET /api/categories");
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID")
    public ResponseEntity<TemirkhanBekzatCategoryResponse> getCategoryById(@PathVariable Long id) {
        log.info("GET /api/categories/{}", id);
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a category")
    public ResponseEntity<TemirkhanBekzatCategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody TemirkhanBekzatCategoryRequest request) {
        log.info("PUT /api/categories/{}", id);
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("DELETE /api/categories/{}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}

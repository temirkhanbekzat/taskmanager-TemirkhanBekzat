package com.bekzat.temirkhan.taskmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TemirkhanBekzatCategoryRequest {

    @NotBlank(message = "Category name is required")
    private String name;

    private String color;

    private String description;
}

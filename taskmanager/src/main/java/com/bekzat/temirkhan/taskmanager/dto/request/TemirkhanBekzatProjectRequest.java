package com.bekzat.temirkhan.taskmanager.dto.request;

import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatProject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TemirkhanBekzatProjectRequest {

    @NotBlank(message = "Project name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    private String description;

    private TemirkhanBekzatProject.ProjectStatus status;
}

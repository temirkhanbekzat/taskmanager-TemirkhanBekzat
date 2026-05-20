package com.bekzat.temirkhan.taskmanager.dto.request;

import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatTask;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TemirkhanBekzatTaskRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    private String description;

    private TemirkhanBekzatTask.TaskStatus status;

    private TemirkhanBekzatTask.Priority priority;

    private LocalDate dueDate;

    private Long projectId;

    private Long assigneeId;

    private Long categoryId;
}

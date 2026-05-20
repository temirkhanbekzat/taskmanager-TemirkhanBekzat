package com.bekzat.temirkhan.taskmanager.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TemirkhanBekzatTaskResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDate dueDate;
    private Long projectId;
    private String projectName;
    private Long assigneeId;
    private String assigneeName;
    private Long creatorId;
    private String creatorName;
    private Long categoryId;
    private String categoryName;
    private int commentsCount;
    private int attachmentsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

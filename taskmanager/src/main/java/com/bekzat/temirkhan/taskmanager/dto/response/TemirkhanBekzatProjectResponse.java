package com.bekzat.temirkhan.taskmanager.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TemirkhanBekzatProjectResponse {
    private Long id;
    private String name;
    private String description;
    private String status;
    private Long ownerId;
    private String ownerName;
    private int taskCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

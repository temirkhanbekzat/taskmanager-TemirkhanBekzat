package com.bekzat.temirkhan.taskmanager.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TemirkhanBekzatAttachmentResponse {
    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private Long taskId;
    private Long uploadedById;
    private String uploadedByName;
    private LocalDateTime createdAt;
}

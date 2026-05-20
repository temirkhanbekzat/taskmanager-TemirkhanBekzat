package com.bekzat.temirkhan.taskmanager.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TemirkhanBekzatCommentResponse {
    private Long id;
    private String content;
    private Long taskId;
    private Long authorId;
    private String authorName;
    private LocalDateTime createdAt;
}

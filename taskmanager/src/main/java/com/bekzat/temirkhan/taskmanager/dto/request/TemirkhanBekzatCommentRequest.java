package com.bekzat.temirkhan.taskmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TemirkhanBekzatCommentRequest {

    @NotBlank(message = "Content is required")
    private String content;
}

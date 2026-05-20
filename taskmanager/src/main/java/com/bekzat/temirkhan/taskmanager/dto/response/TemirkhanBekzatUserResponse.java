package com.bekzat.temirkhan.taskmanager.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TemirkhanBekzatUserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String role;
    private String avatarUrl;
    private boolean active;
    private LocalDateTime createdAt;
}

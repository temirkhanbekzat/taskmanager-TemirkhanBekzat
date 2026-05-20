package com.bekzat.temirkhan.taskmanager.mapper;

import com.bekzat.temirkhan.taskmanager.dto.response.TemirkhanBekzatUserResponse;
import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatUser;
import org.springframework.stereotype.Component;

@Component
public class TemirkhanBekzatUserMapper {

    public TemirkhanBekzatUserResponse toResponse(TemirkhanBekzatUser user) {
        TemirkhanBekzatUserResponse response = new TemirkhanBekzatUserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setRole(user.getRole() != null ? user.getRole().name() : null);
        response.setAvatarUrl(user.getAvatarUrl());
        response.setActive(user.isActive());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}

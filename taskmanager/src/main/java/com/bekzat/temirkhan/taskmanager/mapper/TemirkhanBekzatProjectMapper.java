package com.bekzat.temirkhan.taskmanager.mapper;

import com.bekzat.temirkhan.taskmanager.dto.response.TemirkhanBekzatProjectResponse;
import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatProject;
import org.springframework.stereotype.Component;

@Component
public class TemirkhanBekzatProjectMapper {

    public TemirkhanBekzatProjectResponse toResponse(TemirkhanBekzatProject project) {
        TemirkhanBekzatProjectResponse response = new TemirkhanBekzatProjectResponse();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setStatus(project.getStatus() != null ? project.getStatus().name() : null);
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());

        if (project.getOwner() != null) {
            response.setOwnerId(project.getOwner().getId());
            response.setOwnerName(project.getOwner().getFullName());
        }
        response.setTaskCount(project.getTasks() != null ? project.getTasks().size() : 0);
        return response;
    }
}

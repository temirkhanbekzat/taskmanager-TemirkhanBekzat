package com.bekzat.temirkhan.taskmanager.mapper;

import com.bekzat.temirkhan.taskmanager.dto.response.TemirkhanBekzatTaskResponse;
import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatTask;
import org.springframework.stereotype.Component;

@Component
public class TemirkhanBekzatTaskMapper {

    public TemirkhanBekzatTaskResponse toResponse(TemirkhanBekzatTask task) {
        TemirkhanBekzatTaskResponse response = new TemirkhanBekzatTaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus() != null ? task.getStatus().name() : null);
        response.setPriority(task.getPriority() != null ? task.getPriority().name() : null);
        response.setDueDate(task.getDueDate());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());

        if (task.getProject() != null) {
            response.setProjectId(task.getProject().getId());
            response.setProjectName(task.getProject().getName());
        }
        if (task.getAssignee() != null) {
            response.setAssigneeId(task.getAssignee().getId());
            response.setAssigneeName(task.getAssignee().getFullName());
        }
        if (task.getCreator() != null) {
            response.setCreatorId(task.getCreator().getId());
            response.setCreatorName(task.getCreator().getFullName());
        }
        if (task.getCategory() != null) {
            response.setCategoryId(task.getCategory().getId());
            response.setCategoryName(task.getCategory().getName());
        }
        response.setCommentsCount(task.getComments() != null ? task.getComments().size() : 0);
        response.setAttachmentsCount(task.getAttachments() != null ? task.getAttachments().size() : 0);
        return response;
    }
}

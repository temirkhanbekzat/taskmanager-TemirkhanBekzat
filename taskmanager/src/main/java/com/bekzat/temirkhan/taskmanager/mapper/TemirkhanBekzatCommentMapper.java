package com.bekzat.temirkhan.taskmanager.mapper;

import com.bekzat.temirkhan.taskmanager.dto.response.TemirkhanBekzatCommentResponse;
import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatComment;
import org.springframework.stereotype.Component;

@Component
public class TemirkhanBekzatCommentMapper {

    public TemirkhanBekzatCommentResponse toResponse(TemirkhanBekzatComment comment) {
        TemirkhanBekzatCommentResponse response = new TemirkhanBekzatCommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setCreatedAt(comment.getCreatedAt());

        if (comment.getTask() != null) {
            response.setTaskId(comment.getTask().getId());
        }
        if (comment.getAuthor() != null) {
            response.setAuthorId(comment.getAuthor().getId());
            response.setAuthorName(comment.getAuthor().getFullName());
        }
        return response;
    }
}

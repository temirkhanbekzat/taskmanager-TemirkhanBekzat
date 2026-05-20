package com.bekzat.temirkhan.taskmanager.mapper;

import com.bekzat.temirkhan.taskmanager.dto.response.TemirkhanBekzatCategoryResponse;
import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatCategory;
import org.springframework.stereotype.Component;

@Component
public class TemirkhanBekzatCategoryMapper {

    public TemirkhanBekzatCategoryResponse toResponse(TemirkhanBekzatCategory category) {
        TemirkhanBekzatCategoryResponse response = new TemirkhanBekzatCategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setColor(category.getColor());
        response.setDescription(category.getDescription());
        response.setTaskCount(category.getTasks() != null ? category.getTasks().size() : 0);
        return response;
    }
}

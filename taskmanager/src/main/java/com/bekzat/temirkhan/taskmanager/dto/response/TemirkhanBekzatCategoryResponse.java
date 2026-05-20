package com.bekzat.temirkhan.taskmanager.dto.response;

import lombok.Data;

@Data
public class TemirkhanBekzatCategoryResponse {
    private Long id;
    private String name;
    private String color;
    private String description;
    private int taskCount;
}

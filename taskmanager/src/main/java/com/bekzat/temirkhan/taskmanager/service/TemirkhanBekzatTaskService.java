package com.bekzat.temirkhan.taskmanager.service;

import com.bekzat.temirkhan.taskmanager.dto.request.TemirkhanBekzatTaskRequest;
import com.bekzat.temirkhan.taskmanager.dto.response.TemirkhanBekzatTaskResponse;
import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TemirkhanBekzatTaskService {

    TemirkhanBekzatTaskResponse createTask(TemirkhanBekzatTaskRequest request, String username);

    TemirkhanBekzatTaskResponse getTaskById(Long id);

    Page<TemirkhanBekzatTaskResponse> getTasks(
            String search,
            TemirkhanBekzatTask.TaskStatus status,
            TemirkhanBekzatTask.Priority priority,
            Long projectId,
            Long assigneeId,
            Long categoryId,
            Pageable pageable
    );

    TemirkhanBekzatTaskResponse updateTask(Long id, TemirkhanBekzatTaskRequest request, String username);

    void deleteTask(Long id, String username);

    List<TemirkhanBekzatTaskResponse> getTasksByProject(Long projectId);

    List<TemirkhanBekzatTaskResponse> getMyTasks(String username);

    CompletableFuture<String> sendTaskNotificationAsync(Long taskId, String message);

    CompletableFuture<String> generateTaskReportAsync(Long projectId);
}

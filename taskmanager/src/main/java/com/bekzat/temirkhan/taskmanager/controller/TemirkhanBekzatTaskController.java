package com.bekzat.temirkhan.taskmanager.controller;

import com.bekzat.temirkhan.taskmanager.dto.request.TemirkhanBekzatTaskRequest;
import com.bekzat.temirkhan.taskmanager.dto.response.TemirkhanBekzatTaskResponse;
import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatTask;
import com.bekzat.temirkhan.taskmanager.service.TemirkhanBekzatTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task management endpoints")
public class TemirkhanBekzatTaskController {

    private final TemirkhanBekzatTaskService taskService;

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<TemirkhanBekzatTaskResponse> createTask(
            @Valid @RequestBody TemirkhanBekzatTaskRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("POST /api/tasks by {}", userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(request, userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID")
    public ResponseEntity<TemirkhanBekzatTaskResponse> getTaskById(
            @PathVariable @Parameter(description = "Task ID") Long id) {
        log.info("GET /api/tasks/{}", id);
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping
    @Operation(summary = "Get all tasks with pagination, sorting, search and filtering")
    public ResponseEntity<Page<TemirkhanBekzatTaskResponse>> getTasks(
            @RequestParam(required = false) @Parameter(description = "Search by title or description") String search,
            @RequestParam(required = false) @Parameter(description = "Filter by status") TemirkhanBekzatTask.TaskStatus status,
            @RequestParam(required = false) @Parameter(description = "Filter by priority") TemirkhanBekzatTask.Priority priority,
            @RequestParam(required = false) @Parameter(description = "Filter by project ID") Long projectId,
            @RequestParam(required = false) @Parameter(description = "Filter by assignee ID") Long assigneeId,
            @RequestParam(required = false) @Parameter(description = "Filter by category ID") Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        log.info("GET /api/tasks?search={}&status={}&priority={}&page={}&size={}", search, status, priority, page, size);
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(taskService.getTasks(search, status, priority, projectId, assigneeId, categoryId, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a task")
    public ResponseEntity<TemirkhanBekzatTaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TemirkhanBekzatTaskRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("PUT /api/tasks/{} by {}", id, userDetails.getUsername());
        return ResponseEntity.ok(taskService.updateTask(id, request, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("DELETE /api/tasks/{} by {}", id, userDetails.getUsername());
        taskService.deleteTask(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get tasks by project ID")
    public ResponseEntity<List<TemirkhanBekzatTaskResponse>> getTasksByProject(
            @PathVariable Long projectId) {
        log.info("GET /api/tasks/project/{}", projectId);
        return ResponseEntity.ok(taskService.getTasksByProject(projectId));
    }

    @GetMapping("/my")
    @Operation(summary = "Get tasks assigned to current user")
    public ResponseEntity<List<TemirkhanBekzatTaskResponse>> getMyTasks(
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("GET /api/tasks/my by {}", userDetails.getUsername());
        return ResponseEntity.ok(taskService.getMyTasks(userDetails.getUsername()));
    }

    @PostMapping("/{id}/notify")
    @Operation(summary = "Send async notification for a task")
    public ResponseEntity<Map<String, String>> sendNotification(
            @PathVariable Long id,
            @RequestParam(defaultValue = "Task updated") String message) {
        log.info("POST /api/tasks/{}/notify", id);
        CompletableFuture<String> future = taskService.sendTaskNotificationAsync(id, message);
        return ResponseEntity.accepted().body(Map.of("message", "Notification is being sent asynchronously", "taskId", String.valueOf(id)));
    }

    @GetMapping("/project/{projectId}/report")
    @Operation(summary = "Generate async task report for a project")
    public ResponseEntity<Map<String, String>> generateReport(@PathVariable Long projectId) {
        log.info("GET /api/tasks/project/{}/report", projectId);
        taskService.generateTaskReportAsync(projectId);
        return ResponseEntity.accepted().body(Map.of("message", "Report is being generated asynchronously", "projectId", String.valueOf(projectId)));
    }
}

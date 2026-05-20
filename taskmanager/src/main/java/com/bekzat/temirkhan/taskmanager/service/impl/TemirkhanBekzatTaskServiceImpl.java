package com.bekzat.temirkhan.taskmanager.service.impl;

import com.bekzat.temirkhan.taskmanager.dto.request.TemirkhanBekzatTaskRequest;
import com.bekzat.temirkhan.taskmanager.dto.response.TemirkhanBekzatTaskResponse;
import com.bekzat.temirkhan.taskmanager.entity.*;
import com.bekzat.temirkhan.taskmanager.exception.TemirkhanBekzatBadRequestException;
import com.bekzat.temirkhan.taskmanager.exception.TemirkhanBekzatResourceNotFoundException;
import com.bekzat.temirkhan.taskmanager.mapper.TemirkhanBekzatTaskMapper;
import com.bekzat.temirkhan.taskmanager.repository.*;
import com.bekzat.temirkhan.taskmanager.service.TemirkhanBekzatTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemirkhanBekzatTaskServiceImpl implements TemirkhanBekzatTaskService {

    private final TemirkhanBekzatTaskRepository taskRepository;
    private final TemirkhanBekzatUserRepository userRepository;
    private final TemirkhanBekzatProjectRepository projectRepository;
    private final TemirkhanBekzatCategoryRepository categoryRepository;
    private final TemirkhanBekzatTaskMapper taskMapper;

    @Override
    @Transactional
    public TemirkhanBekzatTaskResponse createTask(TemirkhanBekzatTaskRequest request, String username) {
        log.info("Creating task '{}' by user: {}", request.getTitle(), username);

        TemirkhanBekzatUser creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("User not found: " + username));

        TemirkhanBekzatTask task = new TemirkhanBekzatTask();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : TemirkhanBekzatTask.TaskStatus.TODO);
        task.setPriority(request.getPriority() != null ? request.getPriority() : TemirkhanBekzatTask.Priority.MEDIUM);
        task.setDueDate(request.getDueDate());
        task.setCreator(creator);

        if (request.getProjectId() != null) {
            TemirkhanBekzatProject project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("Project not found: " + request.getProjectId()));
            task.setProject(project);
        }

        if (request.getAssigneeId() != null) {
            TemirkhanBekzatUser assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("Assignee not found: " + request.getAssigneeId()));
            task.setAssignee(assignee);
        }

        if (request.getCategoryId() != null) {
            TemirkhanBekzatCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("Category not found: " + request.getCategoryId()));
            task.setCategory(category);
        }

        TemirkhanBekzatTask saved = taskRepository.save(task);
        log.info("Task created with id: {}", saved.getId());
        return taskMapper.toResponse(saved);
    }

    @Override
    public TemirkhanBekzatTaskResponse getTaskById(Long id) {
        TemirkhanBekzatTask task = taskRepository.findById(id)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("Task not found with id: " + id));
        return taskMapper.toResponse(task);
    }

    @Override
    public Page<TemirkhanBekzatTaskResponse> getTasks(
            String search,
            TemirkhanBekzatTask.TaskStatus status,
            TemirkhanBekzatTask.Priority priority,
            Long projectId,
            Long assigneeId,
            Long categoryId,
            Pageable pageable) {
        log.debug("Fetching tasks with filters: search={}, status={}, priority={}", search, status, priority);
        return taskRepository.findWithFilters(search, status, priority, projectId, assigneeId, categoryId, pageable)
                .map(taskMapper::toResponse);
    }

    @Override
    @Transactional
    public TemirkhanBekzatTaskResponse updateTask(Long id, TemirkhanBekzatTaskRequest request, String username) {
        log.info("Updating task id: {} by user: {}", id, username);

        TemirkhanBekzatTask task = taskRepository.findById(id)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("Task not found with id: " + id));

        task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());

        if (request.getAssigneeId() != null) {
            TemirkhanBekzatUser assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("Assignee not found"));
            task.setAssignee(assignee);
        }

        if (request.getCategoryId() != null) {
            TemirkhanBekzatCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("Category not found"));
            task.setCategory(category);
        }

        TemirkhanBekzatTask updated = taskRepository.save(task);
        log.info("Task updated: {}", updated.getId());
        return taskMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteTask(Long id, String username) {
        log.info("Deleting task id: {} by user: {}", id, username);
        TemirkhanBekzatTask task = taskRepository.findById(id)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("Task not found with id: " + id));
        taskRepository.delete(task);
        log.info("Task deleted: {}", id);
    }

    @Override
    public List<TemirkhanBekzatTaskResponse> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId)
                .stream().map(taskMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<TemirkhanBekzatTaskResponse> getMyTasks(String username) {
        TemirkhanBekzatUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("User not found"));
        return taskRepository.findByAssigneeId(user.getId())
                .stream().map(taskMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<String> sendTaskNotificationAsync(Long taskId, String message) {
        log.info("Async: Sending notification for task id: {}", taskId);
        try {
            Thread.sleep(500); // simulate notification sending
            log.info("Async: Notification sent for task id: {}", taskId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture("Notification sent for task: " + taskId);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<String> generateTaskReportAsync(Long projectId) {
        log.info("Async: Generating report for project id: {}", projectId);
        try {
            Thread.sleep(1000); // simulate report generation
            List<TemirkhanBekzatTask> tasks = taskRepository.findByProjectId(projectId);
            long done = tasks.stream().filter(t -> t.getStatus() == TemirkhanBekzatTask.TaskStatus.DONE).count();
            String report = String.format("Project %d: Total tasks: %d, Done: %d, Completion: %.1f%%",
                    projectId, tasks.size(), done,
                    tasks.isEmpty() ? 0 : (done * 100.0 / tasks.size()));
            log.info("Async: Report generated: {}", report);
            return CompletableFuture.completedFuture(report);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.completedFuture("Report generation interrupted");
        }
    }
}

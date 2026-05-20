package com.bekzat.temirkhan.taskmanager.controller;

import com.bekzat.temirkhan.taskmanager.dto.request.TemirkhanBekzatProjectRequest;
import com.bekzat.temirkhan.taskmanager.dto.response.TemirkhanBekzatProjectResponse;
import com.bekzat.temirkhan.taskmanager.service.impl.TemirkhanBekzatProjectServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Project management endpoints")
public class TemirkhanBekzatProjectController {

    private final TemirkhanBekzatProjectServiceImpl projectService;

    @PostMapping
    @Operation(summary = "Create a new project")
    public ResponseEntity<TemirkhanBekzatProjectResponse> createProject(
            @Valid @RequestBody TemirkhanBekzatProjectRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("POST /api/projects by {}", userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.createProject(request, userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID")
    public ResponseEntity<TemirkhanBekzatProjectResponse> getProjectById(@PathVariable Long id) {
        log.info("GET /api/projects/{}", id);
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @GetMapping
    @Operation(summary = "Get all projects")
    public ResponseEntity<List<TemirkhanBekzatProjectResponse>> getAllProjects() {
        log.info("GET /api/projects");
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/my")
    @Operation(summary = "Get my projects")
    public ResponseEntity<List<TemirkhanBekzatProjectResponse>> getMyProjects(
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("GET /api/projects/my by {}", userDetails.getUsername());
        return ResponseEntity.ok(projectService.getMyProjects(userDetails.getUsername()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a project")
    public ResponseEntity<TemirkhanBekzatProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody TemirkhanBekzatProjectRequest request) {
        log.info("PUT /api/projects/{}", id);
        return ResponseEntity.ok(projectService.updateProject(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.info("DELETE /api/projects/{}", id);
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}

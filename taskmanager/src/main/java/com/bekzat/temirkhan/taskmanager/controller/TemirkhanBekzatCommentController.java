package com.bekzat.temirkhan.taskmanager.controller;

import com.bekzat.temirkhan.taskmanager.dto.request.TemirkhanBekzatCommentRequest;
import com.bekzat.temirkhan.taskmanager.dto.response.TemirkhanBekzatCommentResponse;
import com.bekzat.temirkhan.taskmanager.service.impl.TemirkhanBekzatCommentServiceImpl;
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
@RequestMapping("/api/tasks/{taskId}/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Comment management endpoints")
public class TemirkhanBekzatCommentController {

    private final TemirkhanBekzatCommentServiceImpl commentService;

    @PostMapping
    @Operation(summary = "Add a comment to a task")
    public ResponseEntity<TemirkhanBekzatCommentResponse> addComment(
            @PathVariable Long taskId,
            @Valid @RequestBody TemirkhanBekzatCommentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("POST /api/tasks/{}/comments by {}", taskId, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.addComment(taskId, request, userDetails.getUsername()));
    }

    @GetMapping
    @Operation(summary = "Get all comments for a task")
    public ResponseEntity<List<TemirkhanBekzatCommentResponse>> getComments(@PathVariable Long taskId) {
        log.info("GET /api/tasks/{}/comments", taskId);
        return ResponseEntity.ok(commentService.getCommentsByTask(taskId));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Delete a comment")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("DELETE /api/tasks/{}/comments/{} by {}", taskId, commentId, userDetails.getUsername());
        commentService.deleteComment(commentId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}

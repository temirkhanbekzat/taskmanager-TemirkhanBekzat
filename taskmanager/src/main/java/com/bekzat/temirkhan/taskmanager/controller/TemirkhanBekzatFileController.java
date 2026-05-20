package com.bekzat.temirkhan.taskmanager.controller;

import com.bekzat.temirkhan.taskmanager.dto.response.TemirkhanBekzatAttachmentResponse;
import com.bekzat.temirkhan.taskmanager.service.impl.TemirkhanBekzatFileServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/tasks/{taskId}/files")
@RequiredArgsConstructor
@Tag(name = "Files", description = "File upload and download endpoints")
public class TemirkhanBekzatFileController {

    private final TemirkhanBekzatFileServiceImpl fileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a file to a task")
    public ResponseEntity<TemirkhanBekzatAttachmentResponse> uploadFile(
            @PathVariable Long taskId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("POST /api/tasks/{}/files by {}", taskId, userDetails.getUsername());
        TemirkhanBekzatAttachmentResponse response = fileService.uploadFile(taskId, file, userDetails.getUsername());
        // trigger async processing
        fileService.processFileAsync(response.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all files for a task")
    public ResponseEntity<List<TemirkhanBekzatAttachmentResponse>> getFiles(@PathVariable Long taskId) {
        log.info("GET /api/tasks/{}/files", taskId);
        return ResponseEntity.ok(fileService.getAttachmentsByTask(taskId));
    }

    @GetMapping("/{attachmentId}/download")
    @Operation(summary = "Download a file")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long taskId,
            @PathVariable Long attachmentId) {
        log.info("GET /api/tasks/{}/files/{}/download", taskId, attachmentId);
        Resource resource = fileService.downloadFile(attachmentId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @DeleteMapping("/{attachmentId}")
    @Operation(summary = "Delete a file attachment")
    public ResponseEntity<Map<String, String>> deleteFile(
            @PathVariable Long taskId,
            @PathVariable Long attachmentId) {
        log.info("DELETE /api/tasks/{}/files/{}", taskId, attachmentId);
        fileService.deleteAttachment(attachmentId);
        return ResponseEntity.ok(Map.of("message", "File deleted successfully"));
    }
}

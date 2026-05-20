package com.bekzat.temirkhan.taskmanager.service.impl;

import com.bekzat.temirkhan.taskmanager.dto.response.TemirkhanBekzatAttachmentResponse;
import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatAttachment;
import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatTask;
import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatUser;
import com.bekzat.temirkhan.taskmanager.exception.TemirkhanBekzatBadRequestException;
import com.bekzat.temirkhan.taskmanager.exception.TemirkhanBekzatResourceNotFoundException;
import com.bekzat.temirkhan.taskmanager.repository.TemirkhanBekzatAttachmentRepository;
import com.bekzat.temirkhan.taskmanager.repository.TemirkhanBekzatTaskRepository;
import com.bekzat.temirkhan.taskmanager.repository.TemirkhanBekzatUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemirkhanBekzatFileServiceImpl {

    private final TemirkhanBekzatAttachmentRepository attachmentRepository;
    private final TemirkhanBekzatTaskRepository taskRepository;
    private final TemirkhanBekzatUserRepository userRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Transactional
    public TemirkhanBekzatAttachmentResponse uploadFile(Long taskId, MultipartFile file, String username) {
        log.info("Uploading file '{}' to task id: {} by user: {}", file.getOriginalFilename(), taskId, username);

        if (file.isEmpty()) {
            throw new TemirkhanBekzatBadRequestException("File is empty");
        }

        TemirkhanBekzatTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("Task not found: " + taskId));
        TemirkhanBekzatUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("User not found: " + username));

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            TemirkhanBekzatAttachment attachment = TemirkhanBekzatAttachment.builder()
                    .fileName(file.getOriginalFilename())
                    .filePath(filePath.toString())
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .task(task)
                    .uploadedBy(user)
                    .build();

            TemirkhanBekzatAttachment saved = attachmentRepository.save(attachment);
            log.info("File uploaded successfully: {}", uniqueFileName);

            return toResponse(saved);
        } catch (IOException e) {
            log.error("Failed to upload file: {}", e.getMessage());
            throw new TemirkhanBekzatBadRequestException("Failed to upload file: " + e.getMessage());
        }
    }

    public Resource downloadFile(Long attachmentId) {
        log.info("Downloading file with attachment id: {}", attachmentId);
        TemirkhanBekzatAttachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("Attachment not found: " + attachmentId));
        try {
            Path filePath = Paths.get(attachment.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new TemirkhanBekzatResourceNotFoundException("File not found on disk");
            }
        } catch (MalformedURLException e) {
            throw new TemirkhanBekzatResourceNotFoundException("File not found: " + e.getMessage());
        }
    }

    public List<TemirkhanBekzatAttachmentResponse> getAttachmentsByTask(Long taskId) {
        return attachmentRepository.findByTaskId(taskId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public void deleteAttachment(Long attachmentId) {
        log.info("Deleting attachment id: {}", attachmentId);
        TemirkhanBekzatAttachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("Attachment not found: " + attachmentId));
        try {
            Files.deleteIfExists(Paths.get(attachment.getFilePath()));
        } catch (IOException e) {
            log.warn("Could not delete file from disk: {}", e.getMessage());
        }
        attachmentRepository.delete(attachment);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> processFileAsync(Long attachmentId) {
        log.info("Async: Processing file for attachment id: {}", attachmentId);
        try {
            Thread.sleep(800); // simulate file processing
            log.info("Async: File processing complete for attachment id: {}", attachmentId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture("File processed: " + attachmentId);
    }

    private TemirkhanBekzatAttachmentResponse toResponse(TemirkhanBekzatAttachment a) {
        TemirkhanBekzatAttachmentResponse r = new TemirkhanBekzatAttachmentResponse();
        r.setId(a.getId());
        r.setFileName(a.getFileName());
        r.setFileType(a.getFileType());
        r.setFileSize(a.getFileSize());
        r.setCreatedAt(a.getCreatedAt());
        if (a.getTask() != null) r.setTaskId(a.getTask().getId());
        if (a.getUploadedBy() != null) {
            r.setUploadedById(a.getUploadedBy().getId());
            r.setUploadedByName(a.getUploadedBy().getFullName());
        }
        return r;
    }
}

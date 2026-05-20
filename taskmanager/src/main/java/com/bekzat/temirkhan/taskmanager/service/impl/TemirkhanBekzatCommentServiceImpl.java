package com.bekzat.temirkhan.taskmanager.service.impl;

import com.bekzat.temirkhan.taskmanager.dto.request.TemirkhanBekzatCommentRequest;
import com.bekzat.temirkhan.taskmanager.dto.response.TemirkhanBekzatCommentResponse;
import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatComment;
import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatTask;
import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatUser;
import com.bekzat.temirkhan.taskmanager.exception.TemirkhanBekzatResourceNotFoundException;
import com.bekzat.temirkhan.taskmanager.mapper.TemirkhanBekzatCommentMapper;
import com.bekzat.temirkhan.taskmanager.repository.TemirkhanBekzatCommentRepository;
import com.bekzat.temirkhan.taskmanager.repository.TemirkhanBekzatTaskRepository;
import com.bekzat.temirkhan.taskmanager.repository.TemirkhanBekzatUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemirkhanBekzatCommentServiceImpl {

    private final TemirkhanBekzatCommentRepository commentRepository;
    private final TemirkhanBekzatTaskRepository taskRepository;
    private final TemirkhanBekzatUserRepository userRepository;
    private final TemirkhanBekzatCommentMapper commentMapper;

    @Transactional
    public TemirkhanBekzatCommentResponse addComment(Long taskId, TemirkhanBekzatCommentRequest request, String username) {
        log.info("Adding comment to task id: {} by user: {}", taskId, username);
        TemirkhanBekzatTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("Task not found: " + taskId));
        TemirkhanBekzatUser author = userRepository.findByUsername(username)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("User not found: " + username));

        TemirkhanBekzatComment comment = TemirkhanBekzatComment.builder()
                .content(request.getContent())
                .task(task)
                .author(author)
                .build();
        return commentMapper.toResponse(commentRepository.save(comment));
    }

    public List<TemirkhanBekzatCommentResponse> getCommentsByTask(Long taskId) {
        return commentRepository.findByTaskIdOrderByCreatedAtDesc(taskId)
                .stream().map(commentMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(Long commentId, String username) {
        log.info("Deleting comment id: {} by user: {}", commentId, username);
        TemirkhanBekzatComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("Comment not found: " + commentId));
        commentRepository.delete(comment);
    }
}

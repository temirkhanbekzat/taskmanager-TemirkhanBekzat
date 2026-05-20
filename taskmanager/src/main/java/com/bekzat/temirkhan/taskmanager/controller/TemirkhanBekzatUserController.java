package com.bekzat.temirkhan.taskmanager.controller;

import com.bekzat.temirkhan.taskmanager.dto.response.TemirkhanBekzatUserResponse;
import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatUser;
import com.bekzat.temirkhan.taskmanager.exception.TemirkhanBekzatResourceNotFoundException;
import com.bekzat.temirkhan.taskmanager.mapper.TemirkhanBekzatUserMapper;
import com.bekzat.temirkhan.taskmanager.repository.TemirkhanBekzatUserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
public class TemirkhanBekzatUserController {

    private final TemirkhanBekzatUserRepository userRepository;
    private final TemirkhanBekzatUserMapper userMapper;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<TemirkhanBekzatUserResponse> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("GET /api/users/me by {}", userDetails.getUsername());
        TemirkhanBekzatUser user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("User not found"));
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users (Admin only)")
    public ResponseEntity<List<TemirkhanBekzatUserResponse>> getAllUsers() {
        log.info("GET /api/users (admin)");
        List<TemirkhanBekzatUserResponse> users = userRepository.findAll()
                .stream().map(userMapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<TemirkhanBekzatUserResponse> getUserById(@PathVariable Long id) {
        log.info("GET /api/users/{}", id);
        TemirkhanBekzatUser user = userRepository.findById(id)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("User not found with id: " + id));
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user (Admin only)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE /api/users/{} (admin)", id);
        TemirkhanBekzatUser user = userRepository.findById(id)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }
}

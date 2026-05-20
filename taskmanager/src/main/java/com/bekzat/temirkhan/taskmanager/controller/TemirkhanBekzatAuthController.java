package com.bekzat.temirkhan.taskmanager.controller;

import com.bekzat.temirkhan.taskmanager.dto.request.TemirkhanBekzatLoginRequest;
import com.bekzat.temirkhan.taskmanager.dto.request.TemirkhanBekzatRegisterRequest;
import com.bekzat.temirkhan.taskmanager.dto.response.TemirkhanBekzatAuthResponse;
import com.bekzat.temirkhan.taskmanager.service.TemirkhanBekzatAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Register and login endpoints")
public class TemirkhanBekzatAuthController {

    private final TemirkhanBekzatAuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<TemirkhanBekzatAuthResponse> register(
            @Valid @RequestBody TemirkhanBekzatRegisterRequest request) {
        log.info("Register request for username: {}", request.getUsername());
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Login and get JWT token")
    public ResponseEntity<TemirkhanBekzatAuthResponse> login(
            @Valid @RequestBody TemirkhanBekzatLoginRequest request) {
        log.info("Login request for username: {}", request.getUsername());
        return ResponseEntity.ok(authService.login(request));
    }
}

package com.bekzat.temirkhan.taskmanager.service;

import com.bekzat.temirkhan.taskmanager.dto.request.TemirkhanBekzatLoginRequest;
import com.bekzat.temirkhan.taskmanager.dto.request.TemirkhanBekzatRegisterRequest;
import com.bekzat.temirkhan.taskmanager.dto.response.TemirkhanBekzatAuthResponse;
import com.bekzat.temirkhan.taskmanager.exception.TemirkhanBekzatBadRequestException;
import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatUser;
import com.bekzat.temirkhan.taskmanager.repository.TemirkhanBekzatUserRepository;
import com.bekzat.temirkhan.taskmanager.security.TemirkhanBekzatJwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemirkhanBekzatAuthService {

    private final TemirkhanBekzatUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TemirkhanBekzatJwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Transactional
    public TemirkhanBekzatAuthResponse register(TemirkhanBekzatRegisterRequest request) {
        log.info("Registering new user: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new TemirkhanBekzatBadRequestException("Username already taken: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new TemirkhanBekzatBadRequestException("Email already registered: " + request.getEmail());
        }

        TemirkhanBekzatUser user = TemirkhanBekzatUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(TemirkhanBekzatUser.Role.USER)
                .active(true)
                .build();

        userRepository.save(user);
        log.info("User registered successfully: {}", user.getUsername());

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        return TemirkhanBekzatAuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public TemirkhanBekzatAuthResponse login(TemirkhanBekzatLoginRequest request) {
        log.info("Login attempt for user: {}", request.getUsername());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        TemirkhanBekzatUser user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        log.info("User logged in successfully: {}", user.getUsername());

        return TemirkhanBekzatAuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}

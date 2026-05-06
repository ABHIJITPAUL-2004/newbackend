package com.grihom.backend.controller;

import com.grihom.backend.dto.AuthResponse;
import com.grihom.backend.dto.LoginRequest;
import com.grihom.backend.dto.RegisterRequest;
import com.grihom.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    // POST /api/auth/register → 201 Created
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        log.info("POST /api/auth/register — email: {}", req.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(req));
    }

    // POST /api/auth/login → 200 OK
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        log.info("POST /api/auth/login — email: {}", req.getEmail());
        return ResponseEntity.ok(authService.login(req));
    }
}

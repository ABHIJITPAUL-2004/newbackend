package com.grihom.backend.service;

import com.grihom.backend.dto.AuthResponse;
import com.grihom.backend.dto.LoginRequest;
import com.grihom.backend.dto.RegisterRequest;
import com.grihom.backend.exception.DuplicateResourceException;
import com.grihom.backend.exception.UnauthorizedException;
import com.grihom.backend.exception.ResourceNotFoundException;
import com.grihom.backend.model.Role;
import com.grihom.backend.model.User;
import com.grihom.backend.repository.UserRepository;
import com.grihom.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        log.info("Registering new user: {}", req.getEmail());

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new DuplicateResourceException("An account with email '" + req.getEmail() + "' already exists.");
        }

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(Role.USER)
                .active(true)
                .build();

        User saved = userRepository.save(user);
        String token = jwtUtil.generateToken(saved.getEmail());
        log.info("User registered: {}", saved.getEmail());

        // Send welcome email (async, won't fail registration if it errors)
        emailService.sendWelcomeEmail(saved.getEmail(), saved.getName());

        return toAuthResponse(saved, token);
    }

    public AuthResponse login(LoginRequest req) {
        log.info("Login attempt: {}", req.getEmail());

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No account found for '" + req.getEmail() + "'. Please register first."));

        if (!user.isActive()) {
            throw new UnauthorizedException("Your account has been deactivated. Contact an administrator.");
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            log.warn("Invalid password attempt for: {}", req.getEmail());
            throw new UnauthorizedException("Incorrect password.");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        log.info("Login successful: {}", user.getEmail());
        return toAuthResponse(user, token);
    }

    private AuthResponse toAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .isAdmin(user.getRole() == Role.ADMIN)
                .active(user.isActive())
                .token(token)
                .build();
    }
}

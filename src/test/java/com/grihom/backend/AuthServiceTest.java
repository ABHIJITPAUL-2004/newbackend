package com.grihom.backend;

import com.grihom.backend.dto.LoginRequest;
import com.grihom.backend.dto.RegisterRequest;
import com.grihom.backend.dto.AuthResponse;
import com.grihom.backend.exception.DuplicateResourceException;
import com.grihom.backend.model.Role;
import com.grihom.backend.model.User;
import com.grihom.backend.repository.UserRepository;
import com.grihom.backend.security.JwtUtil;
import com.grihom.backend.service.AuthService;
import com.grihom.backend.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Mock private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private User savedUser;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setName("Ravi Kumar");
        registerRequest.setEmail("ravi@example.com");
        registerRequest.setPassword("password123");

        savedUser = User.builder()
                .id(1L)
                .name("Ravi Kumar")
                .email("ravi@example.com")
                .password("encoded_password")
                .role(Role.USER)
                .active(true)
                .build();
    }

    @Test
    void register_success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtil.generateToken(anyString())).thenReturn("mock_jwt_token");
        doNothing().when(emailService).sendWelcomeEmail(anyString(), anyString());

        AuthResponse response = authService.register(registerRequest);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("ravi@example.com");
        assertThat(response.getToken()).isEqualTo("mock_jwt_token");
        assertThat(response.getRole()).isEqualTo("USER");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_duplicateEmail_throwsException() {
        when(userRepository.existsByEmail("ravi@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void login_success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("ravi@example.com");
        loginRequest.setPassword("password123");

        when(userRepository.findByEmail("ravi@example.com")).thenReturn(Optional.of(savedUser));
        when(passwordEncoder.matches("password123", "encoded_password")).thenReturn(true);
        when(jwtUtil.generateToken(anyString())).thenReturn("mock_jwt_token");

        AuthResponse response = authService.login(loginRequest);

        assertThat(response.getToken()).isEqualTo("mock_jwt_token");
        assertThat(response.getEmail()).isEqualTo("ravi@example.com");
    }
}

package com.grihom.backend.service;

import com.grihom.backend.dto.UserDTO;
import com.grihom.backend.exception.ResourceNotFoundException;
import com.grihom.backend.model.Role;
import com.grihom.backend.model.User;
import com.grihom.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public UserDTO updateRole(Long userId, String role) {
        User user = findById(userId);
        try {
            user.setRole(Role.valueOf(role.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Invalid role: " + role + ". Valid: USER, DECOR, ADMIN");
        }
        User saved = userRepository.save(user);
        log.info("User {} role updated to {}", saved.getEmail(), saved.getRole());
        return toDTO(saved);
    }

    @Transactional
    public UserDTO updateStatus(Long userId, boolean active) {
        User user = findById(userId);
        user.setActive(active);
        User saved = userRepository.save(user);
        log.info("User {} status set to active={}", saved.getEmail(), active);
        return toDTO(saved);
    }

    @Transactional
    public List<UserDTO> deleteUser(Long userId) {
        User user = findById(userId);
        userRepository.delete(user);
        log.info("User deleted: id={} email={}", userId, user.getEmail());
        return getAllUsers();
    }

    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    public UserDTO toDTO(User u) {
        return UserDTO.builder()
                .id(u.getId())
                .name(u.getName())
                .email(u.getEmail())
                .role(u.getRole().name())
                .isAdmin(u.getRole() == Role.ADMIN)
                .active(u.isActive())
                .createdAt(u.getCreatedAt())
                .build();
    }
}

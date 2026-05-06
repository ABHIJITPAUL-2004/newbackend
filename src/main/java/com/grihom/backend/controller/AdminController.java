package com.grihom.backend.controller;

import com.grihom.backend.dto.UpdateRoleRequest;
import com.grihom.backend.dto.UpdateStatusRequest;
import com.grihom.backend.dto.UserDTO;
import com.grihom.backend.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    // GET /api/admin/users → ADMIN, DECOR
    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN','DECOR')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    // PUT /api/admin/users/{id}/role → ADMIN only
    @PutMapping("/users/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoleRequest req) {
        log.info("PUT /api/admin/users/{}/role → {}", id, req.getRole());
        return ResponseEntity.ok(adminService.updateRole(id, req.getRole()));
    }

    // PUT /api/admin/users/{id}/status → ADMIN, DECOR
    @PutMapping("/users/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','DECOR')")
    public ResponseEntity<UserDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest req) {
        log.info("PUT /api/admin/users/{}/status → active={}", id, req.isActive());
        return ResponseEntity.ok(adminService.updateStatus(id, req.isActive()));
    }

    // DELETE /api/admin/users/{id} → ADMIN only
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> deleteUser(@PathVariable Long id) {
        log.info("DELETE /api/admin/users/{}", id);
        return ResponseEntity.ok(adminService.deleteUser(id));
    }
}

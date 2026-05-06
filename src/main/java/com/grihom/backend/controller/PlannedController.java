package com.grihom.backend.controller;

import com.grihom.backend.dto.PlannedDTO;
import com.grihom.backend.dto.PlannedRequest;
import com.grihom.backend.service.PlannedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planned")
@RequiredArgsConstructor
@Slf4j
public class PlannedController {

    private final PlannedService plannedService;

    // GET /api/planned
    @GetMapping
    public ResponseEntity<List<PlannedDTO>> getPlanned(Authentication auth) {
        return ResponseEntity.ok(plannedService.getPlanned(auth.getName()));
    }

    // POST /api/planned
    @PostMapping
    public ResponseEntity<PlannedDTO> addPlanned(
            @Valid @RequestBody PlannedRequest req,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(plannedService.addPlanned(req, auth.getName()));
    }

    // DELETE /api/planned/{improvementId}
    @DeleteMapping("/{improvementId}")
    public ResponseEntity<Void> removePlanned(
            @PathVariable String improvementId,
            Authentication auth) {
        plannedService.removePlanned(improvementId, auth.getName());
        return ResponseEntity.noContent().build();
    }
}

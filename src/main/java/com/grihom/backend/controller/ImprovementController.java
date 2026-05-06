package com.grihom.backend.controller;

import com.grihom.backend.dto.ImprovementDTO;
import com.grihom.backend.dto.ImprovementHistoryDTO;
import com.grihom.backend.dto.ImprovementRequest;
import com.grihom.backend.model.ImprovementHistory;
import com.grihom.backend.model.User;
import com.grihom.backend.repository.ImprovementHistoryRepository;
import com.grihom.backend.repository.UserRepository;
import com.grihom.backend.service.ImprovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/improvements")
@RequiredArgsConstructor
@Slf4j
public class ImprovementController {

    private final ImprovementService improvementService;
    private final ImprovementHistoryRepository historyRepository;
    private final UserRepository userRepository;

    // GET /api/improvements → Public
    @GetMapping
    public ResponseEntity<List<ImprovementDTO>> getAll(
            @RequestParam(required = false) String room,
            @RequestParam(required = false) String cost,
            @RequestParam(required = false) String effort) {
        return ResponseEntity.ok(improvementService.getAll(room, cost, effort));
    }

    // GET /api/improvements/{id} → Public
    @GetMapping("/{id}")
    public ResponseEntity<ImprovementDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(improvementService.getById(id));
    }

    // POST /api/improvements → ADMIN, DECOR
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DECOR')")
    public ResponseEntity<ImprovementDTO> create(
            @Valid @RequestBody ImprovementRequest req,
            Authentication auth) {
        log.info("POST /api/improvements by {}", auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(improvementService.create(req, auth.getName()));
    }

    // PUT /api/improvements/{id} → ADMIN, DECOR
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DECOR')")
    public ResponseEntity<ImprovementDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ImprovementRequest req,
            Authentication auth) {
        log.info("PUT /api/improvements/{} by {}", id, auth.getName());
        return ResponseEntity.ok(improvementService.update(id, req, auth.getName()));
    }

    // DELETE /api/improvements/{id} → ADMIN, DECOR
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DECOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        log.info("DELETE /api/improvements/{} by {}", id, auth.getName());
        improvementService.delete(id, auth.getName());
        return ResponseEntity.noContent().build();
    }

    // GET /api/improvements/history → ADMIN, DECOR
    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('ADMIN','DECOR')")
    public ResponseEntity<List<ImprovementHistoryDTO>> getHistory(
            @RequestParam(required = false, defaultValue = "false") boolean recent) {

        List<ImprovementHistory> list = recent
                ? historyRepository.findTop10ByOrderByPerformedAtDesc()
                : historyRepository.findAllByOrderByPerformedAtDesc();

        List<ImprovementHistoryDTO> dtos = list.stream().map(h ->
                ImprovementHistoryDTO.builder()
                        .id(h.getId())
                        .action(h.getAction())
                        .improvementTitle(h.getImprovementTitle())
                        .details(h.getDetails())
                        .performedByName(h.getPerformedBy() != null ? h.getPerformedBy().getName() : "Unknown")
                        .performedAt(h.getPerformedAt())
                        .build()
        ).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // POST /api/improvements/history → ADMIN, DECOR
    @PostMapping("/history")
    @PreAuthorize("hasAnyRole('ADMIN','DECOR')")
    public ResponseEntity<ImprovementHistoryDTO> addHistory(
            @RequestBody Map<String, String> body,
            Authentication auth) {

        User performer = userRepository.findByEmail(auth.getName()).orElse(null);
        ImprovementHistory entry = ImprovementHistory.builder()
                .action(body.get("action"))
                .improvementTitle(body.get("improvementTitle"))
                .details(body.get("details"))
                .performedBy(performer)
                .build();
        ImprovementHistory saved = historyRepository.save(entry);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ImprovementHistoryDTO.builder()
                        .id(saved.getId())
                        .action(saved.getAction())
                        .improvementTitle(saved.getImprovementTitle())
                        .performedByName(performer != null ? performer.getName() : "Unknown")
                        .performedAt(saved.getPerformedAt())
                        .build()
        );
    }
}

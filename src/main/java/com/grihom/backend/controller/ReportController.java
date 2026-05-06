package com.grihom.backend.controller;

import com.grihom.backend.dto.ReportDTO;
import com.grihom.backend.dto.ReportRequest;
import com.grihom.backend.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportService reportService;

    // GET /api/reports → Authenticated user's reports
    @GetMapping
    public ResponseEntity<List<ReportDTO>> getReports(Authentication auth) {
        log.info("GET /api/reports for {}", auth.getName());
        return ResponseEntity.ok(reportService.getReportsForUser(auth.getName()));
    }

    // POST /api/reports → Save a report
    @PostMapping
    public ResponseEntity<ReportDTO> createReport(
            @Valid @RequestBody ReportRequest req,
            Authentication auth) {
        log.info("POST /api/reports for {}", auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reportService.createReport(req, auth.getName()));
    }

    // DELETE /api/reports/{id} → Delete own report
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id, Authentication auth) {
        log.info("DELETE /api/reports/{} by {}", id, auth.getName());
        reportService.deleteReport(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}

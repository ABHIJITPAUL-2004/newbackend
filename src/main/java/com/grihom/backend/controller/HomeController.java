package com.grihom.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class HomeController {

    // GET /api/test → Public health check
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        log.info("GET /api/test — health check");
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Backend is running",
                "app", "GriHom 2.0",
                "version", "1.0.0",
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}

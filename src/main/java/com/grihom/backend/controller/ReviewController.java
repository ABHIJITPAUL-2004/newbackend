package com.grihom.backend.controller;

import com.grihom.backend.dto.ReviewDTO;
import com.grihom.backend.dto.ReviewRequest;
import com.grihom.backend.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    // GET /api/reviews → Public
    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAll() {
        return ResponseEntity.ok(reviewService.getAll());
    }

    // POST /api/reviews → Public or Authenticated
    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(
            @Valid @RequestBody ReviewRequest req,
            Authentication auth) {
        String email = auth != null ? auth.getName() : null;
        log.info("POST /api/reviews by {}", email != null ? email : "anonymous");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.create(req, email));
    }
}

package com.grihom.backend.service;

import com.grihom.backend.dto.ReviewDTO;
import com.grihom.backend.dto.ReviewRequest;
import com.grihom.backend.model.Review;
import com.grihom.backend.model.User;
import com.grihom.backend.repository.ReviewRepository;
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
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public List<ReviewDTO> getAll() {
        return reviewRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public ReviewDTO create(ReviewRequest req, String email) {
        User user = email != null
                ? userRepository.findByEmail(email).orElse(null)
                : null;

        Review review = Review.builder()
                .name(req.getName())
                .comment(req.getComment())
                .rating(req.getRating())
                .user(user)
                .build();

        Review saved = reviewRepository.save(review);
        log.info("Review submitted by: {}", req.getName());
        return toDTO(saved);
    }

    private ReviewDTO toDTO(Review r) {
        return ReviewDTO.builder()
                .id(r.getId())
                .name(r.getName())
                .comment(r.getComment())
                .rating(r.getRating())
                .createdAt(r.getCreatedAt())
                .build();
    }
}

package com.grihom.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private String name;
    private String comment;
    private Integer rating;
    private LocalDateTime createdAt;
}

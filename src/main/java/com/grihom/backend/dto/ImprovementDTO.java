package com.grihom.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ImprovementDTO {
    private Long id;
    private String title;
    private String description;
    private String room;
    private String cost;
    private String effort;
    private String roi;
    private Integer impact;
    private String duration;
    private String budgetRange;
    private String imageUrl;
    private String source;
    private boolean indianSpecific;
    private String createdByEmail;
    private LocalDateTime createdAt;
}

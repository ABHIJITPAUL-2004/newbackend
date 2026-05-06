package com.grihom.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ImprovementHistoryDTO {
    private Long id;
    private String action;
    private String improvementTitle;
    private String details;
    private String performedByName;
    private LocalDateTime performedAt;
}

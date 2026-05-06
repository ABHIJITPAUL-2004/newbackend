package com.grihom.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ReportDTO {
    private Long id;
    private String title;
    private Integer valorScore;
    private String propertyData;
    private String recommendations;
    private LocalDateTime createdAt;
    private String timestamp;
}

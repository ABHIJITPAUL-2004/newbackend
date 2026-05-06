package com.grihom.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PlannedDTO {
    private Long id;
    private String improvementId;
    private LocalDateTime addedAt;
}

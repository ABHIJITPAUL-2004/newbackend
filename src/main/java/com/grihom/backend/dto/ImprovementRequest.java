package com.grihom.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ImprovementRequest {
    @NotBlank private String title;
    @NotBlank private String description;
    private String room;
    private String cost;
    private String effort;
    private String roi;
    private Integer impact;
    private String duration;
    private String budgetRange;
    private boolean indianSpecific;
    private String imageUrl;
}

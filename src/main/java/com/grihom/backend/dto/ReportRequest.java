package com.grihom.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReportRequest {
    @NotBlank private String title;
    private Integer valorScore;
    private String propertyData;
    private String recommendations;
}

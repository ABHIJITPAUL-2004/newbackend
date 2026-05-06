package com.grihom.backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
public class ReviewRequest {
    @NotBlank private String name;
    private String comment;
    @NotNull @Min(1) @Max(5) private Integer rating;
}

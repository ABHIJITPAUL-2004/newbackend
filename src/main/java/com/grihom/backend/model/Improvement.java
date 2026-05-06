package com.grihom.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "improvements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Improvement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Builder.Default
    private String room = "All";

    @Builder.Default
    private String cost = "Low";

    @Builder.Default
    private String effort = "Medium";

    @Builder.Default
    private String roi = "High";

    @Builder.Default
    private Integer impact = 0;

    private String duration;
    private String budgetRange;
    private String imageUrl;

    @Builder.Default
    private boolean indianSpecific = true;

    @Builder.Default
    private String source = "admin";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdByUser;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

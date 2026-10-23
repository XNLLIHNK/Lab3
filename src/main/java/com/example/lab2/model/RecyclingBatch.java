package com.example.lab2.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "recycling_batches")
@Data
public class RecyclingBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "point_id")
    private CollectionPoint point;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private RecyclingType type;

    private Double mass;

    @Column(nullable = false, updatable = false)
    private LocalDateTime acceptanceDate;

    @Enumerated(EnumType.STRING)
    private BatchStatus status;

    @PrePersist
    protected void onCreate() {
        this.acceptanceDate = LocalDateTime.now();
    }
}
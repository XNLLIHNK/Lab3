package com.example.lab2.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bonus_entries")
@Data
public class BonusEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "batch_id")
    private RecyclingBatch batch;

    private Integer points;

    @Enumerated(EnumType.STRING)
    private BonusStatus status;
}
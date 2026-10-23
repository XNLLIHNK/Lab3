package com.example.lab2.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "recycling_types")
@Data
public class RecyclingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String unit;
    private Double pointsPerKg;
}
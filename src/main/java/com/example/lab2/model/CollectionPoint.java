package com.example.lab2.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "collection_points")
@Data
public class CollectionPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private Boolean isAvailable;
    private Double dailyLimit;

    @ManyToMany
    @JoinTable(
            name = "point_accepted_types",
            joinColumns = @JoinColumn(name = "point_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private List<RecyclingType> acceptedTypes;
}
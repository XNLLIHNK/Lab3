package com.example.lab2.repository;

import com.example.lab2.model.BonusEntry;
import com.example.lab2.model.RecyclingBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BonusEntryRepository extends JpaRepository<BonusEntry, Long> {
    Optional<BonusEntry> findByBatch(RecyclingBatch batch);
}
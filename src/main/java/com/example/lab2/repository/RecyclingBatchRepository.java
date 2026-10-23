package com.example.lab2.repository;

import com.example.lab2.model.BatchStatus;
import com.example.lab2.model.CollectionPoint;
import com.example.lab2.model.RecyclingBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecyclingBatchRepository extends JpaRepository<RecyclingBatch, Long> {
    List<RecyclingBatch> findByStatus(BatchStatus status);
    List<RecyclingBatch> findByPointAndStatus(CollectionPoint point, BatchStatus status);
    List<RecyclingBatch> findByPointAndStatusAndAcceptanceDateAfter(CollectionPoint point, BatchStatus status, LocalDateTime date);
}
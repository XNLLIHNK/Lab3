package com.example.lab2.service;

import com.example.lab2.model.*;
import com.example.lab2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BusinessService {

    @Autowired private RecyclingBatchRepository batchRepo;
    @Autowired private BonusEntryRepository bonusRepo;
    @Autowired private CollectionPointRepository pointRepo;

    @Transactional
    public RecyclingBatch acceptBatch(Long batchId) {
        RecyclingBatch batch = batchRepo.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Партия не найдена"));
        if (batch.getStatus() != BatchStatus.CREATED) {
            throw new RuntimeException("Принять можно только новую партию (CREATED)");
        }

        CollectionPoint point = batch.getPoint();
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();

        List<RecyclingBatch> todayBatches = batchRepo.findByPointAndStatusAndAcceptanceDateAfter(
                point, BatchStatus.ACCEPTED, startOfDay);

        double massToday = todayBatches.stream()
                .mapToDouble(RecyclingBatch::getMass)
                .sum();

        if (massToday + batch.getMass() > point.getDailyLimit()) {
            throw new RuntimeException("Превышен суточный лимит пункта!");
        }

        batch.setStatus(BatchStatus.ACCEPTED);
        batchRepo.save(batch);

        BonusEntry bonus = new BonusEntry();
        bonus.setBatch(batch);
        bonus.setPoints((int) Math.round(batch.getMass() * batch.getType().getPointsPerKg()));
        bonus.setStatus(BonusStatus.ACCRUED);
        bonusRepo.save(bonus);
        return batch;
    }

    @Transactional
    public void cancelBatch(Long batchId) {
        RecyclingBatch batch = batchRepo.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Партия не найдена"));
        if (batch.getStatus() == BatchStatus.CANCELED) {
            throw new RuntimeException("Партия уже отменена");
        }

        bonusRepo.findByBatch(batch).ifPresent(bonus -> {
            if (bonus.getStatus() == BonusStatus.SPENT) {
                throw new RuntimeException("Баллы уже потрачены!");
            }
            bonus.setStatus(BonusStatus.REVOKED);
            bonusRepo.save(bonus);
        });
        batch.setStatus(BatchStatus.CANCELED);
        batchRepo.save(batch);
    }

    @Transactional
    public void closeCollectionPoint(Long pointId) {
        CollectionPoint point = pointRepo.findById(pointId)
                .orElseThrow(() -> new RuntimeException("Пункт не найден"));

        List<RecyclingBatch> pendingBatches = batchRepo.findByPointAndStatus(point, BatchStatus.CREATED);
        if (!pendingBatches.isEmpty()) {
            throw new RuntimeException("Нельзя закрыть пункт: есть непринятые партии");
        }
        point.setIsAvailable(false);
        pointRepo.save(point);
    }

    @Transactional
    public String redeemPoints(Long userId) {
        List<BonusEntry> bonuses = bonusRepo.findAll().stream()
                .filter(b -> b.getBatch().getUser().getId().equals(userId)
                        && b.getStatus() == BonusStatus.ACCRUED)
                .toList();
        int total = bonuses.stream().mapToInt(BonusEntry::getPoints).sum();
        if (total < 1000) throw new RuntimeException("Недостаточно баллов: " + total + "/1000");
        bonuses.forEach(b -> { b.setStatus(BonusStatus.SPENT); bonusRepo.save(b); });
        return "Обменяно " + total + " баллов";
    }

    public Map<String, Double> getStatistics() {
        return batchRepo.findAll().stream()
                .filter(b -> b.getStatus() == BatchStatus.ACCEPTED)
                .collect(Collectors.groupingBy(b -> b.getType().getName(),
                        Collectors.summingDouble(RecyclingBatch::getMass)));
    }
}
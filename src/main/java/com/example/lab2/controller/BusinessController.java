package com.example.lab2.controller;

import com.example.lab2.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/business")
public class BusinessController {

    @Autowired private BusinessService businessService;

    @PostMapping("/accept-batch/{id}")
    public ResponseEntity<String> accept(@PathVariable Long id) {
        businessService.acceptBatch(id);
        return ResponseEntity.ok("Партия принята");
    }

    @PostMapping("/cancel-batch/{id}")
    public ResponseEntity<String> cancel(@PathVariable Long id) {
        businessService.cancelBatch(id);
        return ResponseEntity.ok("Партия отменена");
    }

    @PostMapping("/close-point/{id}")
    public ResponseEntity<String> close(@PathVariable Long id) {
        businessService.closeCollectionPoint(id);
        return ResponseEntity.ok("Пункт закрыт");
    }

    @PostMapping("/redeem-points/{userId}")
    public ResponseEntity<String> redeem(@PathVariable Long userId) {
        return ResponseEntity.ok(businessService.redeemPoints(userId));
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Double>> stats() {
        return ResponseEntity.ok(businessService.getStatistics());
    }
}
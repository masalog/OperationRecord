package com.example.OperationRecord.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "operation_records")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OperationRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long vehicleId;
    private Long driverId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer startMeter;
    private Integer endMeter;
    private double fuelRate;
}


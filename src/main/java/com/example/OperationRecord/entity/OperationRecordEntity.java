package com.example.OperationRecord.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OperationRecordEntity {

    private Long id;
    private Long vehicleId;
    private Long driverId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer startMeter;
    private Integer endMeter;
    private double fuelRate;
}


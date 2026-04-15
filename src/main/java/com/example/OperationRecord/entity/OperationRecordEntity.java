package com.example.OperationRecord.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OperationRecordEntity {

    private Long id;
    private Long vehicleId;
    private Long driverId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer startMeter;
    private Integer endMeter;
    private double fuelRate;

}


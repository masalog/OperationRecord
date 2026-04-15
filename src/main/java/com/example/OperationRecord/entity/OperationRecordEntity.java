package com.example.OperationRecord.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;

@Getter
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

    public OperationRecordEntity(Long id,
                                 Long vehicleId,
                                 Long driverId,
                                 LocalDate date,
                                 LocalTime startTime,
                                 LocalTime endTime,
                                 Integer startMeter,
                                 Integer endMeter,
                                 double fuelRate) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startMeter = startMeter;
        this.endMeter = endMeter;
        this.fuelRate = fuelRate;
    }

}


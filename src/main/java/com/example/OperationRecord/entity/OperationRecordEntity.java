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
    private String startLocation;
    private String endLocation;
    private double distance;

    public OperationRecordEntity(Long id,
                                 Long vehicleId,
                                 Long driverId,
                                 LocalDate date,
                                 LocalTime startTime,
                                 LocalTime endTime,
                                 String startLocation,
                                 String endLocation,
                                 double distance) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.distance = distance;
    }

}


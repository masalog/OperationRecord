package com.example.OperationRecord.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationRecordResponse {

    private Long id;
    private Long vehicleId;
    private Long driverId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer startMeter;
    private Integer endMeter;
    private Double fuelRate;

}

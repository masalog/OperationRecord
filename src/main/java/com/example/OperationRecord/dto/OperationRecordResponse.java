package com.example.OperationRecord.dto;

import java.time.LocalDateTime;

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
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer startMeter;
    private Integer endMeter;
    private Double fuelRate;

}

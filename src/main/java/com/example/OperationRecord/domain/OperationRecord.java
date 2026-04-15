package com.example.OperationRecord.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import lombok.Getter;

@Getter
public class OperationRecord {

    private final Long id;
    private final Long vehicleId;
    private final Long driverId;
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final String startLocation;
    private final String endLocation;
    private final double distance;

    public OperationRecord(Long id,
                           Long vehicleId,
                           Long driverId,
                           LocalDate date,
                           LocalTime startTime,
                           LocalTime endTime,
                           String startLocation,
                           String endLocation,
                           double distance) {

        Objects.requireNonNull(vehicleId, "vehicleId must not be null");
        Objects.requireNonNull(driverId, "driverId must not be null");
        Objects.requireNonNull(date, "date must not be null");
        Objects.requireNonNull(startTime, "startTime must not be null");
        Objects.requireNonNull(endTime, "endTime must not be null");
        Objects.requireNonNull(startLocation, "startLocation must not be null");
        Objects.requireNonNull(endLocation, "endLocation must not be null");

        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("開始時刻は終了時刻より前でなければならない");
        }

        if (distance < 0) {
            throw new IllegalArgumentException("距離は0以上でなければならない");
        }

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


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
    private final Integer startMeter;
    private final Integer endMeter;
    private final Double fuelRate;

    public OperationRecord(Long id,
                           Long vehicleId,
                           Long driverId,
                           LocalDate date,
                           LocalTime startTime,
                           LocalTime endTime,
                           Integer startMeter,
                           Integer endMeter,
                           Double fuelRate) {

        Objects.requireNonNull(vehicleId, "vehicleId must not be null");
        Objects.requireNonNull(driverId, "driverId must not be null");
        Objects.requireNonNull(date, "date must not be null");
        Objects.requireNonNull(startTime, "startTime must not be null");
        Objects.requireNonNull(endTime, "endTime must not be null");
        Objects.requireNonNull(startMeter, "startMeter must not be null");
        Objects.requireNonNull(endMeter, "endMeter must not be null");
        Objects.requireNonNull(fuelRate, "fuelRate must not be null");

        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("開始時刻は終了時刻より前でなければならない");
        }

        if (startMeter < 0 || endMeter < 0) {
            throw new IllegalArgumentException("メーター値は0以上でなければならない");
        }

        if (startMeter > endMeter) {
            throw new IllegalArgumentException("開始メーターは終了メーター以下でなければならない");
        }

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

    /** 派生値：走行距離 */
    public int getDistance() {
        return endMeter - startMeter;
    }
}
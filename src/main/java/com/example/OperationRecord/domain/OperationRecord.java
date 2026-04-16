package com.example.OperationRecord.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Getter;

@Getter
public class OperationRecord {

    private final Long id;
    private final Long vehicleId;
    private final Long driverId;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final Integer startMeter;
    private final Integer endMeter;
    private final Double fuelRate;

    public OperationRecord(Long id,
                           Long vehicleId,
                           Long driverId,
                           LocalDateTime startDateTime,
                           LocalDateTime endDateTime,
                           Integer startMeter,
                           Integer endMeter,
                           Double fuelRate) {

        // --- 必須チェック ---
        Objects.requireNonNull(vehicleId, "vehicleId must not be null");
        Objects.requireNonNull(driverId, "driverId must not be null");
        Objects.requireNonNull(startDateTime, "startDateTime must not be null");
        Objects.requireNonNull(endDateTime, "endDateTime must not be null");
        Objects.requireNonNull(startMeter, "startMeter must not be null");
        Objects.requireNonNull(endMeter, "endMeter must not be null");
        Objects.requireNonNull(fuelRate, "fuelRate must not be null");

        // --- 日時の整合性チェック ---
        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("開始日時は終了日時より前でなければならない");
        }

        // --- メーター値チェック ---
        if (startMeter < 0 || endMeter < 0) {
            throw new IllegalArgumentException("メーター値は0以上でなければならない");
        }

        if (startMeter > endMeter) {
            throw new IllegalArgumentException("開始メーターは終了メーター以下でなければならない");
        }

        this.id = id;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.startMeter = startMeter;
        this.endMeter = endMeter;
        this.fuelRate = fuelRate;
    }

    /** 派生値：走行距離 */
    public int getDistance() {
        return endMeter - startMeter;
    }

    /** 派生値：運行時間（Duration） */
    public Duration getDuration() {
        return Duration.between(startDateTime, endDateTime);
    }

}

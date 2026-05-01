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
    private LocalDateTime endDateTime;   // ← 後半でセットするので final を外す
    private final Integer startMeter;
    private Integer endMeter;            // ← 後半でセットするので final を外す
    private Double fuelRate;             // ← 後半でセットするので final を外す

    public OperationRecord(Long id,
                           Long vehicleId,
                           Long driverId,
                           LocalDateTime startDateTime,
                           LocalDateTime endDateTime,
                           Integer startMeter,
                           Integer endMeter,
                           Double fuelRate) {

        // --- 前半の必須チェック ---
        Objects.requireNonNull(vehicleId, "vehicleId must not be null");
        Objects.requireNonNull(driverId, "driverId must not be null");
        Objects.requireNonNull(startDateTime, "startDateTime must not be null");
        Objects.requireNonNull(startMeter, "startMeter must not be null");

        // --- 後半は null 許可（更新時にチェックする） ---
        if (endDateTime != null && startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("開始日時は終了日時より前でなければならない");
        }

        if (endMeter != null && startMeter > endMeter) {
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

    // --- 後半更新メソッド ---
    public void updateEndInfo(LocalDateTime endDateTime,
                              Integer endMeter,
                              Double fuelRate) {

        Objects.requireNonNull(endDateTime, "endDateTime must not be null");
        Objects.requireNonNull(endMeter, "endMeter must not be null");
        Objects.requireNonNull(fuelRate, "fuelRate must not be null");

        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("開始日時は終了日時より前でなければならない");
        }

        if (startMeter > endMeter) {
            throw new IllegalArgumentException("開始メーターは終了メーター以下でなければならない");
        }

        this.endDateTime = endDateTime;
        this.endMeter = endMeter;
        this.fuelRate = fuelRate;
    }

    /** 派生値：走行距離 */
    public int getDistance() {
        if (endMeter == null) return 0;
        return endMeter - startMeter;
    }

    /** 派生値：運行時間（Duration） */
    public Duration getDuration() {
        if (endDateTime == null) return Duration.ZERO;
        return Duration.between(startDateTime, endDateTime);
    }
}

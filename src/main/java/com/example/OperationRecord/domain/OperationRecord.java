package com.example.OperationRecord.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import com.example.OperationRecord.exception.BadRequestException;

import lombok.Getter;

@Getter
public class OperationRecord {

    private final Long id;
    private final Long vehicleId;
    private final Long driverId;
    private final LocalDateTime startDateTime;
    private final Integer startMeter;   // ★ 前半で確定

    // 後半で更新される項目
    private LocalDateTime endDateTime;
    private Integer endMeter;

    /** 前半登録用 */
    public OperationRecord(Long id,
                           Long vehicleId,
                           Long driverId,
                           LocalDateTime startDateTime,
                           Integer startMeter) {

        Objects.requireNonNull(vehicleId, "vehicleId must not be null");
        Objects.requireNonNull(driverId, "driverId must not be null");
        Objects.requireNonNull(startDateTime, "startDateTime must not be null");
        Objects.requireNonNull(startMeter, "startMeter must not be null");

        this.id = id;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.startDateTime = startDateTime;
        this.startMeter = startMeter;
    }

    /** 後半更新（帰還時） */
    public void updateEndInfo(LocalDateTime endDateTime, Integer endMeter) {
        Objects.requireNonNull(endDateTime, "endDateTime must not be null");
        Objects.requireNonNull(endMeter, "endMeter must not be null");

        if (startDateTime.isAfter(endDateTime)) {
            throw new BadRequestException("開始日時は終了日時より前でなければならない");
        }
        if (startMeter > endMeter) {
            throw new BadRequestException("開始メーターは終了メーター以下でなければならない");
        }

        this.endDateTime = endDateTime;
        this.endMeter = endMeter;
    }

    /** 派生値：走行距離 */
    public int getDistance() {
        if (endMeter == null) return 0;
        return endMeter - startMeter;
    }

    /** 派生値：運行時間 */
    public Duration getDuration() {
        if (endDateTime == null) return Duration.ZERO;
        return Duration.between(startDateTime, endDateTime);
    }
}
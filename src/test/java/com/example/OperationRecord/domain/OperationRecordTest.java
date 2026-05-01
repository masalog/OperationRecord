package com.example.OperationRecord.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class OperationRecordTest {

    @Test
    void 前半保存_終了情報がnullでも生成できる() {
        OperationRecord record = new OperationRecord(
                1L,
                10L,
                20L,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                null,      // endDateTime
                1000,
                null,      // endMeter
                null       // fuelRate
        );

        assertEquals(0, record.getDistance());
        assertEquals(Duration.ZERO, record.getDuration());
    }

    @Test
    void 後半更新で正常に値がセットされる() {
        OperationRecord record = new OperationRecord(
                1L,
                10L,
                20L,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                null,
                1000,
                null,
                null
        );

        record.updateEndInfo(
                LocalDateTime.of(2026, 1, 1, 12, 0),
                1200,
                5.0
        );

        assertEquals(200, record.getDistance());
        assertEquals(Duration.ofHours(2), record.getDuration());
    }

    @Test
    void updateEndInfo_開始日時より終了日時が前なら例外() {
        OperationRecord record = new OperationRecord(
                1L,
                10L,
                20L,
                LocalDateTime.of(2026, 1, 1, 12, 0),
                null,
                1000,
                null,
                null
        );

        assertThrows(IllegalArgumentException.class, () -> {
            record.updateEndInfo(
                    LocalDateTime.of(2026, 1, 1, 10, 0),
                    1100,
                    5.0
            );
        });
    }

    @Test
    void updateEndInfo_開始メーターより終了メーターが小さいなら例外() {
        OperationRecord record = new OperationRecord(
                1L,
                10L,
                20L,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                null,
                1200,
                null,
                null
        );

        assertThrows(IllegalArgumentException.class, () -> {
            record.updateEndInfo(
                    LocalDateTime.of(2026, 1, 1, 12, 0),
                    1100,
                    5.0
            );
        });
    }

    @Test
    void 必須項目がnullなら例外() {
        assertThrows(NullPointerException.class, () -> {
            new OperationRecord(
                    1L,
                    null, // vehicleId
                    20L,
                    LocalDateTime.now(),
                    null,
                    1000,
                    null,
                    null
            );
        });
    }
}

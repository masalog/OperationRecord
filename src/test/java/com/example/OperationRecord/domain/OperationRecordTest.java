package com.example.OperationRecord.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class OperationRecordTest {

    @Test
    void 前半生成_必須項目だけで生成できる() {
        OperationRecord record = new OperationRecord(
                1L,
                10L,
                20L,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                1000     // startMeter（前半必須）
        );

        assertEquals(0, record.getDistance());
        assertEquals(Duration.ZERO, record.getDuration());
        assertNull(record.getEndDateTime());
        assertNull(record.getEndMeter());
    }

    @Test
    void 後半更新で正常に値がセットされる() {
        OperationRecord record = new OperationRecord(
                1L,
                10L,
                20L,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                1000
        );

        record.updateEndInfo(
                LocalDateTime.of(2026, 1, 1, 12, 0),
                1200
        );

        assertEquals(200, record.getDistance());
        assertEquals(Duration.ofHours(2), record.getDuration());
        assertEquals(1200, record.getEndMeter());
    }

    @Test
    void updateEndInfo_開始日時より終了日時が前なら例外() {
        OperationRecord record = new OperationRecord(
                1L,
                10L,
                20L,
                LocalDateTime.of(2026, 1, 1, 12, 0),
                1000
        );

        assertThrows(IllegalArgumentException.class, () -> {
            record.updateEndInfo(
                    LocalDateTime.of(2026, 1, 1, 10, 0),
                    1100
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
                1200
        );

        assertThrows(IllegalArgumentException.class, () -> {
            record.updateEndInfo(
                    LocalDateTime.of(2026, 1, 1, 12, 0),
                    1100
            );
        });
    }

    @Test
    void 前半生成_必須項目がnullなら例外() {
        assertThrows(NullPointerException.class, () -> {
            new OperationRecord(
                    1L,
                    null, // vehicleId
                    20L,
                    LocalDateTime.now(),
                    1000
            );
        });
    }
}

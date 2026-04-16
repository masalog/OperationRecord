package com.example.OperationRecord.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class OperationRecordTest {

    @Test
    void 正常に生成できる() {
        OperationRecord record = new OperationRecord(
                1L,
                10L,
                20L,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                LocalDateTime.of(2024, 1, 1, 12, 0),
                1000,
                1200,
                5.0
        );

        assertEquals(200, record.getDistance());
        assertEquals(Duration.ofHours(2), record.getDuration());
    }

    @Test
    void 日をまたぐ運行でも正常に生成できる() {
        OperationRecord record = new OperationRecord(
                1L,
                10L,
                20L,
                LocalDateTime.of(2024, 1, 1, 23, 0),
                LocalDateTime.of(2024, 1, 2, 2, 0),
                1000,
                1100,
                5.0
        );

        assertEquals(100, record.getDistance());
        assertEquals(Duration.ofHours(3), record.getDuration());
    }

    @Test
    void 開始日時が終了日時より後なら例外() {
        assertThrows(IllegalArgumentException.class, () -> {
            new OperationRecord(
                    1L,
                    10L,
                    20L,
                    LocalDateTime.of(2024, 1, 1, 12, 0),
                    LocalDateTime.of(2024, 1, 1, 10, 0),
                    1000,
                    1100,
                    5.0
            );
        });
    }

    @Test
    void メーター値が負なら例外() {
        assertThrows(IllegalArgumentException.class, () -> {
            new OperationRecord(
                    1L,
                    10L,
                    20L,
                    LocalDateTime.of(2024, 1, 1, 10, 0),
                    LocalDateTime.of(2024, 1, 1, 12, 0),
                    -1,
                    1100,
                    5.0
            );
        });
    }

    @Test
    void 開始メーターが終了メーターより大きいなら例外() {
        assertThrows(IllegalArgumentException.class, () -> {
            new OperationRecord(
                    1L,
                    10L,
                    20L,
                    LocalDateTime.of(2024, 1, 1, 10, 0),
                    LocalDateTime.of(2024, 1, 1, 12, 0),
                    1200,
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
                    LocalDateTime.now(),
                    1000,
                    1100,
                    5.0
            );
        });
    }
}

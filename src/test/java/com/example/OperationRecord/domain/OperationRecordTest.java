package com.example.OperationRecord.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.example.OperationRecord.exception.BadRequestException;

class OperationRecordTest {

    @Test
    void 前半生成_必須項目だけで生成できる() {
        OperationRecord record = new OperationRecord(
                1L,
                10L,
                20L,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                1000
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
    void updateEndInfo_開始日時より終了日時が前ならBadRequestException() {
        OperationRecord record = new OperationRecord(
                1L,
                10L,
                20L,
                LocalDateTime.of(2026, 1, 1, 12, 0),
                1000
        );

        BadRequestException e = assertThrows(BadRequestException.class, () -> {
            record.updateEndInfo(
                    LocalDateTime.of(2026, 1, 1, 10, 0),
                    1100
            );
        });

        assertEquals("開始日時は終了日時より前でなければならない", e.getMessage());
    }

    @Test
    void updateEndInfo_開始メーターより終了メーターが小さいならBadRequestException() {
        OperationRecord record = new OperationRecord(
                1L,
                10L,
                20L,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                1200
        );

        BadRequestException e = assertThrows(BadRequestException.class, () -> {
            record.updateEndInfo(
                    LocalDateTime.of(2026, 1, 1, 12, 0),
                    1100
            );
        });

        assertEquals("開始メーターは終了メーター以下でなければならない", e.getMessage());
    }

    @Test
    void 前半生成_必須項目がnullならNullPointerException() {
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

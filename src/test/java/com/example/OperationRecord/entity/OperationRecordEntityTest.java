package com.example.OperationRecord.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class OperationRecordEntityTest {

    @Test
    void 前半データのみでも生成できて値を保持する() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);

        OperationRecordEntity entity = new OperationRecordEntity(
                1L,
                10L,
                20L,
                start,
                null,   // endDateTime（後半未入力）
                1000,
                null    // endMeter（後半未入力）
        );

        assertEquals(1L, entity.getId());
        assertEquals(10L, entity.getVehicleId());
        assertEquals(20L, entity.getDriverId());
        assertEquals(start, entity.getStartDateTime());
        assertNull(entity.getEndDateTime());
        assertEquals(1000, entity.getStartMeter());
        assertNull(entity.getEndMeter());
    }

    @Test
    void 後半データが揃っている場合も正しく保持する() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end   = LocalDateTime.of(2026, 1, 1, 12, 0);

        OperationRecordEntity entity = new OperationRecordEntity(
                1L,
                10L,
                20L,
                start,
                end,
                1000,
                1200
        );

        assertEquals(end, entity.getEndDateTime());
        assertEquals(1200, entity.getEndMeter());
    }
}

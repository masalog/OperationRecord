package com.example.OperationRecord.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class OperationRecordEntityTest {

    @Test
    void 正常に生成できて値が保持される() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 12, 0);

        OperationRecordEntity entity = new OperationRecordEntity(
                1L,
                10L,
                20L,
                start,
                end,
                1000,
                1200,
                5.0
        );

        assertEquals(1L, entity.getId());
        assertEquals(10L, entity.getVehicleId());
        assertEquals(20L, entity.getDriverId());
        assertEquals(start, entity.getStartDateTime());
        assertEquals(end, entity.getEndDateTime());
        assertEquals(1000, entity.getStartMeter());
        assertEquals(1200, entity.getEndMeter());
        assertEquals(5.0, entity.getFuelRate());
    }
}

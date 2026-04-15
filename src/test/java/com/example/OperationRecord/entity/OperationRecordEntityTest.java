package com.example.OperationRecord.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

class OperationRecordEntityTest {

    @Test
    void エンティティが正しく生成される() {

        OperationRecordEntity entity = new OperationRecordEntity(
                100L,
                1L,
                10L,
                LocalDate.of(2026, 4, 15),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                "福岡市",
                "北九州市",
                65.2
        );

        assertEquals(100L, entity.getId());
        assertEquals(1L, entity.getVehicleId());
        assertEquals(10L, entity.getDriverId());
        assertEquals(LocalDate.of(2026, 4, 15), entity.getDate());
        assertEquals(LocalTime.of(9, 0), entity.getStartTime());
        assertEquals(LocalTime.of(18, 0), entity.getEndTime());
        assertEquals("福岡市", entity.getStartLocation());
        assertEquals("北九州市", entity.getEndLocation());
        assertEquals(65.2, entity.getDistance());
    }
}



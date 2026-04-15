package com.example.OperationRecord.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

class OperationRecordTest {

    @Test
    void 正常に運行記録を生成できる() {

        OperationRecord record = new OperationRecord(
                100L,
                1L,   // vehicleId
                10L,  // driverId
                LocalDate.of(2026, 4, 15),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                12000,   // startMeter
                12100,   // endMeter
                165.5    // fuelRate
        );

        assertEquals(100L, record.getId());
        assertEquals(1L, record.getVehicleId());
        assertEquals(10L, record.getDriverId());
        assertEquals(LocalDate.of(2026, 4, 15), record.getDate());
        assertEquals(LocalTime.of(9, 0), record.getStartTime());
        assertEquals(LocalTime.of(18, 0), record.getEndTime());
        assertEquals(12000, record.getStartMeter());
        assertEquals(12100, record.getEndMeter());
        assertEquals(165.5, record.getFuelRate());
        assertEquals(100, record.getDistance()); // endMeter - startMeter
    }

    @Test
    void 開始時刻より終了時刻が前なら例外を投げる() {

        assertThrows(IllegalArgumentException.class, () -> {
            new OperationRecord(
                    100L,
                    1L,
                    10L,
                    LocalDate.of(2026, 4, 15),
                    LocalTime.of(18, 0),  // NG
                    LocalTime.of(9, 0),
                    12000,
                    12100,
                    165.5
            );
        });
    }

    @Test
    void 開始メーターが終了メーターより大きい場合は例外を投げる() {

        assertThrows(IllegalArgumentException.class, () -> {
            new OperationRecord(
                    100L,
                    1L,
                    10L,
                    LocalDate.of(2026, 4, 15),
                    LocalTime.of(9, 0),
                    LocalTime.of(18, 0),
                    13000,   // NG
                    12100,
                    165.5
            );
        });
    }
}


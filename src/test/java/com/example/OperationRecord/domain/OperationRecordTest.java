package com.example.OperationRecord.domain;

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
                "福岡市",
                "北九州市",
                65.2
        );

        assertEquals(100L, record.getId());
        assertEquals(1L, record.getVehicleId());
        assertEquals(10L, record.getDriverId());
        assertEquals(LocalDate.of(2026, 4, 15), record.getDate());
        assertEquals(LocalTime.of(9, 0), record.getStartTime());
        assertEquals(LocalTime.of(18, 0), record.getEndTime());
        assertEquals("福岡市", record.getStartLocation());
        assertEquals("北九州市", record.getEndLocation());
        assertEquals(65.2, record.getDistance());
    }

    @Test
    void 開始時刻より終了時刻が前なら例外を投げる() {

        assertThrows(IllegalArgumentException.class, () -> {
            new OperationRecord(
                    100L,
                    1L,
                    10L,
                    LocalDate.of(2026, 4, 15),
                    LocalTime.of(18, 0),
                    LocalTime.of(9, 0), // NG
                    "福岡市",
                    "北九州市",
                    65.2
            );
        });
    }

    @Test
    void 距離がマイナスなら例外を投げる() {

        assertThrows(IllegalArgumentException.class, () -> {
            new OperationRecord(
                    100L,
                    1L,
                    10L,
                    LocalDate.of(2026, 4, 15),
                    LocalTime.of(9, 0),
                    LocalTime.of(18, 0),
                    "福岡市",
                    "北九州市",
                    -10.0 // NG
            );
        });
    }
}

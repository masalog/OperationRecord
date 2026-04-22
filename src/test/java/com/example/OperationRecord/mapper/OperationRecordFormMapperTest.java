package com.example.OperationRecord.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.example.OperationRecord.dto.OperationRecordForm;
import com.example.OperationRecord.dto.OperationRecordRequest;

class OperationRecordFormMapperTest {

    @Test
    void フォームをリクエストDTOへ正しく変換できること() {
        OperationRecordForm form = new OperationRecordForm();
        form.setDriverId("1");
        form.setVehicleId("2");

        // ✅ LocalDateTime.parse() が標準で読める ISO-8601 形式（T区切り）に修正
        form.setStartDateTime("2026-04-18T09:00");
        form.setEndDateTime("2026-04-18T18:00");

        form.setStartMeter("１２３４５"); // 全角
        form.setEndMeter("12350");      // 半角
        form.setFuelRate("8.0");

        OperationRecordRequest req = OperationRecordFormMapper.fromFormToRequest(form);

        assertEquals(1L, req.getDriverId());
        assertEquals(2L, req.getVehicleId());
        assertEquals(LocalDateTime.of(2026, 4, 18, 9, 0), req.getStartDateTime());
        assertEquals(LocalDateTime.of(2026, 4, 18, 18, 0), req.getEndDateTime());
        assertEquals(12345, req.getStartMeter()); // 全角→半角変換されている
        assertEquals(12350, req.getEndMeter());
        assertEquals(8.0, req.getFuelRate());
    }
}
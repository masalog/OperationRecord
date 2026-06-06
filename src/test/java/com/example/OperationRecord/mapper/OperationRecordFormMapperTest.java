package com.example.OperationRecord.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.example.OperationRecord.dto.OperationRecordForm;
import com.example.OperationRecord.dto.OperationRecordRequest;
import com.example.OperationRecord.exception.BadRequestException;

class OperationRecordFormMapperTest {

    @Test
    void 前半保存用フォームが正しくリクエストに変換される() {

        OperationRecordForm form = new OperationRecordForm();
        form.setDriverId("１");           // 全角
        form.setVehicleId("２");         // 全角
        form.setStartDateTime("2026-04-18T09:00");
        form.setStartMeter("１２３４５"); // 全角

        OperationRecordRequest request =
                OperationRecordFormMapper.toRegisterRequest(form);

        assertEquals(1L, request.getDriverId());
        assertEquals(2L, request.getVehicleId());
        assertEquals(LocalDateTime.of(2026, 4, 18, 9, 0),
                     request.getStartDateTime());
        assertEquals(12345, request.getStartMeter());

        // 後半項目は未設定
        assertNull(request.getEndDateTime());
        assertNull(request.getEndMeter());
    }

    @Test
    void 後半更新用フォームが正しくリクエストに変換される() {

        OperationRecordForm form = new OperationRecordForm();
        form.setDriverId("10");
        form.setVehicleId("20");
        form.setStartDateTime("2026-04-18T09:00");
        form.setStartMeter("1000");     // 前半で入力済み

        form.setEndDateTime("2026-04-18T18:00");
        form.setEndMeter("１２３００"); // 全角

        OperationRecordRequest request =
                OperationRecordFormMapper.toUpdateRequest(form, 99L);

        assertEquals(99L, request.getOperationRecordId());
        assertEquals(10L, request.getDriverId());
        assertEquals(20L, request.getVehicleId());

        // 前半の値が保持されている
        assertEquals(1000, request.getStartMeter());
        assertEquals(LocalDateTime.of(2026, 4, 18, 9, 0),
                     request.getStartDateTime());

        // 後半の値が反映されている
        assertEquals(LocalDateTime.of(2026, 4, 18, 18, 0),
                     request.getEndDateTime());
        assertEquals(12300, request.getEndMeter());
    }
}

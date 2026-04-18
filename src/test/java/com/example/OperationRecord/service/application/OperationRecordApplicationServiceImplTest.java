package com.example.OperationRecord.service.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.OperationRecord.domain.OperationRecord;
import com.example.OperationRecord.dto.OperationRecordRequest;
import com.example.OperationRecord.dto.OperationRecordResponse;
import com.example.OperationRecord.service.operationRecord.OperationRecordService;

class OperationRecordApplicationServiceImplTest {

    @Test
    void 登録処理が実行されレスポンスが返る() {

        // フェイクのドメインサービス
        OperationRecordService fakeService = new OperationRecordService() {
            @Override
            public OperationRecord regist(OperationRecord domain) {
                return new OperationRecord(
                        1L,
                        domain.getVehicleId(),
                        domain.getDriverId(),
                        domain.getStartDateTime(),
                        domain.getEndDateTime(),
                        domain.getStartMeter(),
                        domain.getEndMeter(),
                        domain.getFuelRate()
                );
            }

            @Override
            public OperationRecord findById(Long id) {
                return null;
            }

            @Override
            public List<OperationRecord> findAll() {
                return List.of();
            }

            @Override
            public void remove(Long id) {
            }
        };

        OperationRecordApplicationServiceImpl app =
                new OperationRecordApplicationServiceImpl(fakeService);

        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 12, 0);

        OperationRecordRequest request = new OperationRecordRequest(
                10L, 20L, start, end, 1000, 1200, 5.0
        );

        OperationRecordResponse response = app.register(request);

        assertEquals(1L, response.getId());
    }
}



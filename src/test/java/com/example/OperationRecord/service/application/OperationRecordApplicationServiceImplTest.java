package com.example.OperationRecord.service.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.OperationRecord.domain.OperationRecord;
import com.example.OperationRecord.dto.OperationRecordRequest;
import com.example.OperationRecord.dto.OperationRecordResponse;

class OperationRecordApplicationServiceImplTest {

    @Test
    void 新規登録すると運行記録がID付きで返る() {

        // フェイクのドメインサービス
        OperationRecordService fakeService = new OperationRecordService() {

            @Override
            public OperationRecord regist(OperationRecord domain) {
                // 登録後は ID が採番される想定
                return new OperationRecord(
                        1L,
                        domain.getVehicleId(),
                        domain.getDriverId(),
                        domain.getStartDateTime(),
                        domain.getStartMeter()
                );
            }

            @Override
            public OperationRecord update(OperationRecord domain) {
                return domain;
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

        OperationRecordApplicationServiceImpl applicationService =
                new OperationRecordApplicationServiceImpl(fakeService);

        // 前半登録リクエスト
        OperationRecordRequest request = new OperationRecordRequest();
        request.setOperationRecordId(null); // 新規登録
        request.setVehicleId(10L);
        request.setDriverId(20L);
        request.setStartDateTime(LocalDateTime.of(2026, 1, 1, 10, 0));
        request.setStartMeter(1000);

        OperationRecordResponse response =
                applicationService.register(request);

        assertEquals(1L, response.getId());
    }
}
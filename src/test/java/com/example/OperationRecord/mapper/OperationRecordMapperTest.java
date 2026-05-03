package com.example.OperationRecord.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.example.OperationRecord.domain.OperationRecord;
import com.example.OperationRecord.dto.OperationRecordRequest;
import com.example.OperationRecord.dto.OperationRecordResponse;
import com.example.OperationRecord.entity.OperationRecordEntity;

class OperationRecordMapperTest {

    @Test
    void ドメインをエンティティに変換できる() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end   = LocalDateTime.of(2026, 1, 1, 12, 0);

        OperationRecord domain = new OperationRecord(
                1L,
                10L,
                20L,
                start,
                1000
        );
        domain.updateEndInfo(end, 1200);

        OperationRecordEntity entity =
                OperationRecordMapper.fromDomainToEntity(domain);

        assertEquals(1L, entity.getId());
        assertEquals(10L, entity.getVehicleId());
        assertEquals(20L, entity.getDriverId());
        assertEquals(start, entity.getStartDateTime());
        assertEquals(end, entity.getEndDateTime());
        assertEquals(1000, entity.getStartMeter());
        assertEquals(1200, entity.getEndMeter());
    }

    @Test
    void エンティティからドメインへ前半データを変換できる() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);

        OperationRecordEntity entity = new OperationRecordEntity(
                1L,
                10L,
                20L,
                start,
                null,
                1000,
                null
        );

        OperationRecord domain =
                OperationRecordMapper.fromEntityToDomain(entity);

        assertEquals(1L, domain.getId());
        assertEquals(10L, domain.getVehicleId());
        assertEquals(20L, domain.getDriverId());
        assertEquals(start, domain.getStartDateTime());
        assertEquals(1000, domain.getStartMeter());
        assertNull(domain.getEndDateTime());
        assertNull(domain.getEndMeter());
    }

    @Test
    void エンティティからドメインへ後半データを含めて変換できる() {
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

        OperationRecord domain =
                OperationRecordMapper.fromEntityToDomain(entity);

        assertEquals(end, domain.getEndDateTime());
        assertEquals(1200, domain.getEndMeter());
    }

    @Test
    void リクエストからドメインへ前半登録用に変換できる() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);

        OperationRecordRequest request = new OperationRecordRequest();
        request.setOperationRecordId(null);
        request.setVehicleId(10L);
        request.setDriverId(20L);
        request.setStartDateTime(start);
        request.setStartMeter(1000);

        OperationRecord domain =
                OperationRecordMapper.fromRequestToDomain(request);

        assertNull(domain.getId());
        assertEquals(10L, domain.getVehicleId());
        assertEquals(20L, domain.getDriverId());
        assertEquals(start, domain.getStartDateTime());
        assertEquals(1000, domain.getStartMeter());
    }

    @Test
    void リクエストからドメインへ後半更新用に変換できる() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end   = LocalDateTime.of(2026, 1, 1, 12, 0);

        OperationRecordRequest request = new OperationRecordRequest();
        request.setOperationRecordId(1L);
        request.setVehicleId(10L);
        request.setDriverId(20L);
        request.setStartDateTime(start);
        request.setStartMeter(1000);
        request.setEndDateTime(end);
        request.setEndMeter(1200);

        OperationRecord domain =
                OperationRecordMapper.fromRequestToDomain(request);

        assertEquals(1L, domain.getId());
        assertEquals(end, domain.getEndDateTime());
        assertEquals(1200, domain.getEndMeter());
    }

    @Test
    void ドメインをレスポンスに変換できる() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);

        OperationRecord domain = new OperationRecord(
                1L,
                10L,
                20L,
                start,
                1000
        );

        OperationRecordResponse response =
                OperationRecordMapper.fromDomainToResponse(domain);

        assertEquals(1L, response.getId());
        assertEquals(10L, response.getVehicleId());
        assertEquals(20L, response.getDriverId());
        assertEquals(start, response.getStartDateTime());
        assertEquals(1000, response.getStartMeter());
    }
}

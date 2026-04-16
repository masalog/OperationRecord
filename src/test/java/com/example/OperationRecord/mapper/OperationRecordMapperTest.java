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
    void DomainからEntityへ正しく変換できる() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 12, 0);

        OperationRecord domain = new OperationRecord(
                1L,
                10L,
                20L,
                start,
                end,
                1000,
                1200,
                5.0
        );

        OperationRecordEntity entity = OperationRecordMapper.fromDomainToEntity(domain);

        assertEquals(1L, entity.getId());
        assertEquals(10L, entity.getVehicleId());
        assertEquals(20L, entity.getDriverId());
        assertEquals(start, entity.getStartDateTime());
        assertEquals(end, entity.getEndDateTime());
        assertEquals(1000, entity.getStartMeter());
        assertEquals(1200, entity.getEndMeter());
        assertEquals(5.0, entity.getFuelRate());
    }

    @Test
    void EntityからDomainへ正しく変換できる() {
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

        OperationRecord domain = OperationRecordMapper.fromEntityToDomain(entity);

        assertEquals(1L, domain.getId());
        assertEquals(10L, domain.getVehicleId());
        assertEquals(20L, domain.getDriverId());
        assertEquals(start, domain.getStartDateTime());
        assertEquals(end, domain.getEndDateTime());
        assertEquals(1000, domain.getStartMeter());
        assertEquals(1200, domain.getEndMeter());
        assertEquals(5.0, domain.getFuelRate());
    }

    @Test
    void RequestからDomainへ正しく変換できる() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 12, 0);

        OperationRecordRequest request = new OperationRecordRequest(
                10L,
                20L,
                start,
                end,
                1000,
                1200,
                5.0
        );

        OperationRecord domain = OperationRecordMapper.fromRequestToDomain(request);

        assertNull(domain.getId()); // 新規登録なので null
        assertEquals(10L, domain.getVehicleId());
        assertEquals(20L, domain.getDriverId());
        assertEquals(start, domain.getStartDateTime());
        assertEquals(end, domain.getEndDateTime());
        assertEquals(1000, domain.getStartMeter());
        assertEquals(1200, domain.getEndMeter());
        assertEquals(5.0, domain.getFuelRate());
    }

    @Test
    void DomainからResponseへ正しく変換できる() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 12, 0);

        OperationRecord domain = new OperationRecord(
                1L,
                10L,
                20L,
                start,
                end,
                1000,
                1200,
                5.0
        );

        OperationRecordResponse response = OperationRecordMapper.fromDomainToResponse(domain);

        assertEquals(1L, response.getId());
        assertEquals(10L, response.getVehicleId());
        assertEquals(20L, response.getDriverId());
        assertEquals(start, response.getStartDateTime());
        assertEquals(end, response.getEndDateTime());
        assertEquals(1000, response.getStartMeter());
        assertEquals(1200, response.getEndMeter());
        assertEquals(5.0, response.getFuelRate());
    }
}

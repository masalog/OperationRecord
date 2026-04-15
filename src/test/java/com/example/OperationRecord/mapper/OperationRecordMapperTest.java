package com.example.OperationRecord.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import com.example.OperationRecord.domain.OperationRecord;
import com.example.OperationRecord.dto.OperationRecordRequest;
import com.example.OperationRecord.dto.OperationRecordResponse;
import com.example.OperationRecord.entity.OperationRecordEntity;

class OperationRecordMapperTest {

    @Test
    void ドメインからエンティティに変換できる() {

        OperationRecord domain = new OperationRecord(
                100L,
                1L,
                10L,
                LocalDate.of(2026, 4, 15),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                12000,
                12100,
                165.5
        );

        OperationRecordEntity entity = OperationRecordMapper.fromDomainToEntity(domain);

        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getVehicleId(), entity.getVehicleId());
        assertEquals(domain.getDriverId(), entity.getDriverId());
        assertEquals(domain.getDate(), entity.getDate());
        assertEquals(domain.getStartTime(), entity.getStartTime());
        assertEquals(domain.getEndTime(), entity.getEndTime());
        assertEquals(domain.getStartMeter(), entity.getStartMeter());
        assertEquals(domain.getEndMeter(), entity.getEndMeter());
        assertEquals(domain.getFuelRate(), entity.getFuelRate());
    }

    @Test
    void エンティティからドメインに変換できる() {

        OperationRecordEntity entity = new OperationRecordEntity(
                100L,
                1L,
                10L,
                LocalDate.of(2026, 4, 15),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                12000,
                12100,
                165.5
        );

        OperationRecord domain = OperationRecordMapper.fromEntityToDomain(entity);

        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getVehicleId(), domain.getVehicleId());
        assertEquals(entity.getDriverId(), domain.getDriverId());
        assertEquals(entity.getDate(), domain.getDate());
        assertEquals(entity.getStartTime(), domain.getStartTime());
        assertEquals(entity.getEndTime(), domain.getEndTime());
        assertEquals(entity.getStartMeter(), domain.getStartMeter());
        assertEquals(entity.getEndMeter(), domain.getEndMeter());
        assertEquals(entity.getFuelRate(), domain.getFuelRate());
    }

    @Test
    void リクエストDTOからドメインに変換できる() {

        OperationRecordRequest request = new OperationRecordRequest(
                1L,
                10L,
                LocalDate.of(2026, 4, 15),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                12000,
                12100,
                165.5
        );

        OperationRecord domain = OperationRecordMapper.fromRequestToDomain(request);

        assertEquals(null, domain.getId()); // 新規登録なので null
        assertEquals(request.getVehicleId(), domain.getVehicleId());
        assertEquals(request.getDriverId(), domain.getDriverId());
        assertEquals(request.getDate(), domain.getDate());
        assertEquals(request.getStartTime(), domain.getStartTime());
        assertEquals(request.getEndTime(), domain.getEndTime());
        assertEquals(request.getStartMeter(), domain.getStartMeter());
        assertEquals(request.getEndMeter(), domain.getEndMeter());
        assertEquals(request.getFuelRate(), domain.getFuelRate());
    }

    @Test
    void ドメインからレスポンスDTOに変換できる() {

        OperationRecord domain = new OperationRecord(
                100L,
                1L,
                10L,
                LocalDate.of(2026, 4, 15),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                12000,
                12100,
                165.5
        );

        OperationRecordResponse response = OperationRecordMapper.fromDomainToResponse(domain);

        assertEquals(domain.getId(), response.getId());
        assertEquals(domain.getVehicleId(), response.getVehicleId());
        assertEquals(domain.getDriverId(), response.getDriverId());
        assertEquals(domain.getDate(), response.getDate());
        assertEquals(domain.getStartTime(), response.getStartTime());
        assertEquals(domain.getEndTime(), response.getEndTime());
        assertEquals(domain.getStartMeter(), response.getStartMeter());
        assertEquals(domain.getEndMeter(), response.getEndMeter());
        assertEquals(domain.getFuelRate(), response.getFuelRate());
    }

}

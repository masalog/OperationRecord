package com.example.OperationRecord.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import com.example.OperationRecord.domain.OperationRecord;
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
}

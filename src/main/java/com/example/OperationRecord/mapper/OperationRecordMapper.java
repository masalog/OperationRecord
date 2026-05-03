package com.example.OperationRecord.mapper;

import com.example.OperationRecord.domain.OperationRecord;
import com.example.OperationRecord.dto.OperationRecordRequest;
import com.example.OperationRecord.dto.OperationRecordResponse;
import com.example.OperationRecord.entity.OperationRecordEntity;

public class OperationRecordMapper {

    /** Domain → Entity */
    public static OperationRecordEntity fromDomainToEntity(OperationRecord domain) {
        return new OperationRecordEntity(
                domain.getId(),
                domain.getVehicleId(),
                domain.getDriverId(),
                domain.getStartDateTime(),
                domain.getEndDateTime(),
                domain.getStartMeter(),
                domain.getEndMeter()
        );
    }

    /** Entity → Domain */
    public static OperationRecord fromEntityToDomain(OperationRecordEntity entity) {
        OperationRecord domain = new OperationRecord(
                entity.getId(),
                entity.getVehicleId(),
                entity.getDriverId(),
                entity.getStartDateTime(),
                entity.getStartMeter()
        );

        if (entity.getEndDateTime() != null && entity.getEndMeter() != null) {
            domain.updateEndInfo(entity.getEndDateTime(), entity.getEndMeter());
        }
        return domain;
    }

    /** Request → Domain（新規・更新どちらにも対応） */
    public static OperationRecord fromRequestToDomain(OperationRecordRequest request) {
        OperationRecord domain = new OperationRecord(
                request.getOperationRecordId(),
                request.getVehicleId(),
                request.getDriverId(),
                request.getStartDateTime(),
                request.getStartMeter()
        );

        if (request.getEndDateTime() != null && request.getEndMeter() != null) {
            domain.updateEndInfo(request.getEndDateTime(), request.getEndMeter());
        }
        return domain;
    }

    /** Domain → Response */
    public static OperationRecordResponse fromDomainToResponse(OperationRecord domain) {
        return new OperationRecordResponse(
                domain.getId(),
                domain.getVehicleId(),
                domain.getDriverId(),
                domain.getStartDateTime(),
                domain.getEndDateTime(),
                domain.getStartMeter(),
                domain.getEndMeter()
        );
    }
}
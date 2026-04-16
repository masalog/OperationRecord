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
                domain.getEndMeter(),
                domain.getFuelRate()
        );
    }

    /** Entity → Domain */
    public static OperationRecord fromEntityToDomain(OperationRecordEntity entity) {
        return new OperationRecord(
                entity.getId(),
                entity.getVehicleId(),
                entity.getDriverId(),
                entity.getStartDateTime(),
                entity.getEndDateTime(),
                entity.getStartMeter(),
                entity.getEndMeter(),
                entity.getFuelRate()
        );
    }

    /** Request → Domain（新規登録用） */
    public static OperationRecord fromRequestToDomain(OperationRecordRequest request) {
        return new OperationRecord(
                null,  // 新規登録なので ID は null
                request.getVehicleId(),
                request.getDriverId(),
                request.getStartDateTime(),
                request.getEndDateTime(),
                request.getStartMeter(),
                request.getEndMeter(),
                request.getFuelRate()
        );
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
                domain.getEndMeter(),
                domain.getFuelRate()
        );
    }
}

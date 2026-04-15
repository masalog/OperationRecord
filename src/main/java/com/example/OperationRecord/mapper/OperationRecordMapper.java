package com.example.OperationRecord.mapper;

import com.example.OperationRecord.domain.OperationRecord;
import com.example.OperationRecord.dto.OperationRecordRequest;
import com.example.OperationRecord.dto.OperationRecordResponse;
import com.example.OperationRecord.entity.OperationRecordEntity;

public class OperationRecordMapper {

    public static OperationRecordEntity fromDomainToEntity(OperationRecord domain) {
        return new OperationRecordEntity(
                domain.getId(),
                domain.getVehicleId(),
                domain.getDriverId(),
                domain.getDate(),
                domain.getStartTime(),
                domain.getEndTime(),
                domain.getStartMeter(),
                domain.getEndMeter(),
                domain.getFuelRate()
        );
    }

    public static OperationRecord fromEntityToDomain(OperationRecordEntity entity) {
        return new OperationRecord(
                entity.getId(),
                entity.getVehicleId(),
                entity.getDriverId(),
                entity.getDate(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getStartMeter(),
                entity.getEndMeter(),
                entity.getFuelRate()
        );
    }

    public static OperationRecord fromRequestToDomain(OperationRecordRequest request) {
        return new OperationRecord(
                null,  // 新規登録なので ID は null
                request.getVehicleId(),
                request.getDriverId(),
                request.getDate(),
                request.getStartTime(),
                request.getEndTime(),
                request.getStartMeter(),
                request.getEndMeter(),
                request.getFuelRate()
        );
    }

    public static OperationRecordResponse fromDomainToResponse(OperationRecord domain) {
        return new OperationRecordResponse(
                domain.getId(),
                domain.getVehicleId(),
                domain.getDriverId(),
                domain.getDate(),
                domain.getStartTime(),
                domain.getEndTime(),
                domain.getStartMeter(),
                domain.getEndMeter(),
                domain.getFuelRate()
        );
    }

}

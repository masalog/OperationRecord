package com.example.OperationRecord.repository;

import java.util.*;
import org.springframework.stereotype.Repository;

import com.example.OperationRecord.domain.OperationRecord;
import com.example.OperationRecord.entity.OperationRecordEntity;
import com.example.OperationRecord.mapper.OperationRecordMapper;

@Repository
public class InMemoryOperationRecordRepository implements OperationRecordRepository {

    private final Map<Long, OperationRecordEntity> store = new HashMap<>();
    private long sequence = 1L;

    @Override
    public OperationRecord save(OperationRecord domain) {

        // Domain → Entity
        OperationRecordEntity entity = OperationRecordMapper.fromDomainToEntity(domain);

        Long id = entity.getId();
        if (id == null) {
            id = sequence++;
        }

        OperationRecordEntity saved = new OperationRecordEntity(
                id,
                entity.getVehicleId(),
                entity.getDriverId(),
                entity.getDate(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getStartMeter(),
                entity.getEndMeter(),
                entity.getFuelRate()
        );

        store.put(id, saved);

        // Entity → Domain
        return OperationRecordMapper.fromEntityToDomain(saved);
    }

    @Override
    public OperationRecord findById(Long id) {
        OperationRecordEntity entity = store.get(id);
        if (entity == null) {
            return null; // 本番では例外にする
        }
        return OperationRecordMapper.fromEntityToDomain(entity);
    }

    @Override
    public List<OperationRecord> findAll() {
        return store.values().stream()
                .map(OperationRecordMapper::fromEntityToDomain)
                .toList();
    }

    @Override
    public void remove(Long id) {
        store.remove(id);
    }

    /** テスト用ユーティリティ（interface には含めない） */
    public void clear() {
        store.clear();
    }
}
package com.example.OperationRecord.repository;

import java.util.*;

import org.springframework.stereotype.Repository;

import com.example.OperationRecord.entity.OperationRecordEntity;

@Repository
public class InMemoryOperationRecordRepository implements OperationRecordRepository {

    private final Map<Long, OperationRecordEntity> store = new HashMap<>();
    private long sequence = 1L;

    @Override
    public OperationRecordEntity insert(OperationRecordEntity entity) {

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
        return saved;
    }

    @Override
    public OperationRecordEntity selectById(Long id) {
        return store.get(id);
    }

    @Override
    public List<OperationRecordEntity> selectAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    /** テスト用ユーティリティ（interface には含めない） */
    public void clear() {
        store.clear();
    }
}

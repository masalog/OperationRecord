package com.example.OperationRecord.repository;

import java.util.List;

import com.example.OperationRecord.entity.OperationRecordEntity;

public interface OperationRecordRepository {

    OperationRecordEntity save(OperationRecordEntity entity);

    OperationRecordEntity findById(Long id);

    List<OperationRecordEntity> findAll();

    void deleteById(Long id);

}
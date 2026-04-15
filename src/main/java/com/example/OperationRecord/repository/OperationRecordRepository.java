package com.example.OperationRecord.repository;

import java.util.List;

import com.example.OperationRecord.entity.OperationRecordEntity;

public interface OperationRecordRepository {

    OperationRecordEntity insert(OperationRecordEntity entity);

    OperationRecordEntity selectById(Long id);

    List<OperationRecordEntity> selectAll();

    void deleteById(Long id);

}
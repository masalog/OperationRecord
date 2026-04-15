package com.example.OperationRecord.service;

import java.util.List;

import com.example.OperationRecord.entity.OperationRecordEntity;

public interface OperationRecordService {

    OperationRecordEntity register(OperationRecordEntity entity);

    OperationRecordEntity find(Long id);

    List<OperationRecordEntity> findAll();

    void delete(Long id);
}


package com.example.OperationRecord.service;

import java.util.List;

import com.example.OperationRecord.entity.OperationRecordEntity;

public interface OperationRecordService {

    OperationRecordEntity regist(OperationRecordEntity entity);

    OperationRecordEntity findById(Long id);

    List<OperationRecordEntity> findAll();

    void remove(Long id);
}


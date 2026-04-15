package com.example.OperationRecord.repository;

import java.util.List;

import com.example.OperationRecord.domain.OperationRecord;

public interface OperationRecordRepository {

    OperationRecord save(OperationRecord domain);

    OperationRecord findById(Long id);

    List<OperationRecord> findAll();

    void remove(Long id);
}
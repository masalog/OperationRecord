package com.example.OperationRecord.service;

import java.util.List;

import com.example.OperationRecord.domain.OperationRecord;

public interface OperationRecordService {

    OperationRecord regist(OperationRecord domain);

    OperationRecord findById(Long id);

    List<OperationRecord> findAll();

    void remove(Long id);

}


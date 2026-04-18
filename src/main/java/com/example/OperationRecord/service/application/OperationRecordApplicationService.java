package com.example.OperationRecord.service.application;

import com.example.OperationRecord.dto.OperationRecordRequest;
import com.example.OperationRecord.dto.OperationRecordResponse;

public interface OperationRecordApplicationService {
    OperationRecordResponse register(OperationRecordRequest request);
}

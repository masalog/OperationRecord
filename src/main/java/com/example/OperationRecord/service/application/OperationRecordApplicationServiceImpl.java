package com.example.OperationRecord.service.application;

import org.springframework.stereotype.Service;

import com.example.OperationRecord.domain.OperationRecord;
import com.example.OperationRecord.dto.OperationRecordRequest;
import com.example.OperationRecord.dto.OperationRecordResponse;
import com.example.OperationRecord.mapper.OperationRecordMapper;
import com.example.OperationRecord.service.operationRecord.OperationRecordService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OperationRecordApplicationServiceImpl implements OperationRecordApplicationService {

    private final OperationRecordService operationRecordService;

    @Override
    public OperationRecordResponse register(OperationRecordRequest request) {

        // Request → Domain
        OperationRecord domain = OperationRecordMapper.fromRequestToDomain(request);

        // Domain を登録
        OperationRecord saved = operationRecordService.regist(domain);

        // Domain → Response
        return OperationRecordMapper.fromDomainToResponse(saved);
    }
}


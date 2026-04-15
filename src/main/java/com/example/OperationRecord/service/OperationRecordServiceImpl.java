package com.example.OperationRecord.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.OperationRecord.domain.OperationRecord;
import com.example.OperationRecord.repository.OperationRecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OperationRecordServiceImpl implements OperationRecordService {

    private final OperationRecordRepository repository;

    @Override
    public OperationRecord regist(OperationRecord domain) {
        return repository.save(domain);
    }

    @Override
    public OperationRecord findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<OperationRecord> findAll() {
        return repository.findAll();
    }

    @Override
    public void remove(Long id) {
        repository.remove(id);
    }
}
package com.example.OperationRecord.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.OperationRecord.entity.OperationRecordEntity;
import com.example.OperationRecord.repository.OperationRecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OperationRecordServiceImpl implements OperationRecordService {

    private final OperationRecordRepository repository;

    @Override
    public OperationRecordEntity regist(OperationRecordEntity entity) {
        return repository.insert(entity);
    }

    @Override
    public OperationRecordEntity findById(Long id) {
        return repository.selectById(id);
    }

    @Override
    public List<OperationRecordEntity> findAll() {
        return repository.selectAll();
    }

    @Override
    public void remove(Long id) {
        repository.deleteById(id);
    }
}


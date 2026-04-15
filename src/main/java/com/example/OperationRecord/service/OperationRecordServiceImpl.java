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
    public OperationRecordEntity register(OperationRecordEntity entity) {
        return repository.save(entity);
    }

    @Override
    public OperationRecordEntity find(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<OperationRecordEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}


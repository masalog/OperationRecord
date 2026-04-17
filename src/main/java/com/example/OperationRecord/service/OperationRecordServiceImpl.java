package com.example.OperationRecord.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.OperationRecord.domain.OperationRecord;
import com.example.OperationRecord.entity.OperationRecordEntity;
import com.example.OperationRecord.mapper.OperationRecordMapper;
import com.example.OperationRecord.repository.OperationRecordJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OperationRecordServiceImpl implements OperationRecordService {

    private final OperationRecordJpaRepository repository;

    @Override
    public OperationRecord regist(OperationRecord domain) {

        // Domain → Entity
        OperationRecordEntity entity = OperationRecordMapper.fromDomainToEntity(domain);

        // DB 保存
        OperationRecordEntity saved = repository.save(entity);

        // Entity → Domain
        return OperationRecordMapper.fromEntityToDomain(saved);
    }

    @Override
    public OperationRecord findById(Long id) {
        return repository.findById(id)
                .map(OperationRecordMapper::fromEntityToDomain)
                .orElseThrow(() -> new RuntimeException("Record not found"));
    }

    @Override
    public List<OperationRecord> findAll() {
        return repository.findAll().stream()
                .map(OperationRecordMapper::fromEntityToDomain)
                .toList();
    }

    @Override
    public void remove(Long id) {
        repository.deleteById(id);
    }
}

